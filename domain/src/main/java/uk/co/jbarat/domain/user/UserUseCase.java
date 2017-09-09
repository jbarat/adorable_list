package uk.co.jbarat.domain.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.subjects.BehaviorSubject;
import uk.co.jbarat.domain.post.Post;
import uk.co.jbarat.domain.scope.Activity;

import static java.util.Collections.emptyMap;

@Activity
public class UserUseCase {

    private static final Map<Integer, User> EMPTY = emptyMap();

    private final BehaviorSubject<Map<Integer, User>> cache = BehaviorSubject.createDefault(EMPTY);
    private final UserDataSource userDataSource;
    private final Scheduler scheduler;

    @Inject
    UserUseCase(UserDataSource userDataSource, @Named("io") Scheduler scheduler) {
        this.userDataSource = userDataSource;
        this.scheduler = scheduler;
    }

    /**
     * Tries to get data from the cache first if it's empty then it goes to the data source.
     */
    public Single<User> getUser(int id) {
        return Single.concat(getCache(), getDataSource())
                .filter(userMap -> !userMap.isEmpty())
                .firstOrError() // We cannot recover
                .map(integerUserMap -> integerUserMap.get(id))
                .subscribeOn(scheduler);
    }

    /**
     * If it gets data it maps it into a map for easier access by id.
     */
    private Single<Map<Integer, User>> getDataSource() {
        return userDataSource.getAllUsers()
                .map(this::transformIntoMap)
                .doOnSuccess(userMap -> {
                    if (!userMap.isEmpty()) {
                        cache.onNext(userMap);
                    }
                });
    }

    /**
     * Very simple cache
     */
    private Single<Map<Integer, User>> getCache() {
        return cache.first(EMPTY);
    }

    /**
     * Maps the users into a map for easier id based access.
     */
    private Map<Integer, User> transformIntoMap(List<User> users) {
        Map<Integer, User> integerUserMap = new HashMap<>();

        for (User user : users) {
            integerUserMap.put(user.getId(), user);
        }

        return integerUserMap;
    }

    public interface UserDataSource {
        Single<List<User>> getAllUsers();
    }
}
