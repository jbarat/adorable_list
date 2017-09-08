package uk.co.jbarat.domain.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import uk.co.jbarat.domain.scope.Activity;

@Activity
public class UserUseCase {

    private final Scheduler scheduler;
    private final Single<Map<Integer, User>> cachedData;

    @Inject
    UserUseCase(UserDataSource userDataSource, @Named("io") Scheduler scheduler) {
        this.scheduler = scheduler;

        cachedData = userDataSource.getAllUsers()
                .map(this::transformIntoMap)
                .cache();
    }

    public Single<User> getUser(int id) {
        return cachedData
                .map(integerUserMap -> integerUserMap.get(id))
                .subscribeOn(scheduler);
    }

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
