package uk.co.jbarat.domain.post;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import uk.co.jbarat.domain.scope.Activity;

import static java.util.Collections.emptyList;

@Activity
public class PostUseCase {

    private static final List<Post> EMPTY = emptyList();

    private final BehaviorSubject<List<Post>> cache = BehaviorSubject.createDefault(EMPTY);
    private final PostDataSource postDataSource;
    private final Scheduler scheduler;

    @Inject
    PostUseCase(PostDataSource postDataSource, @Named("io") Scheduler scheduler) {
        this.postDataSource = postDataSource;
        this.scheduler = scheduler;
    }

    /**
     * Tries to get data from the cache first if it's empty then it goes to the data source.
     */
    public Single<List<Post>> getAllPosts() {
        return Single.concat(getCache(), getDataSource())
                .filter(posts -> !posts.isEmpty())
                .first(EMPTY)
                .subscribeOn(scheduler);
    }

    /**
     * Goes trough the posts and returns the one with the correct id or throws
     */
    public Single<Post> getPost(int postId) {
        return getAllPosts()
                .flattenAsObservable(posts -> posts)
                .filter(post -> post.getId() == postId)
                .firstOrError();
    }

    /**
     * Very simple cache
     */
    private Single<List<Post>> getCache() {
        return cache.first(EMPTY);
    }

    /**
     * If it successfully gets data from the data source it updates the cache.
     */
    private Single<List<Post>> getDataSource() {
        return postDataSource.getAllPosts()
                .doOnSuccess(posts -> {
                    if (!posts.isEmpty()) {
                        cache.onNext(posts);
                    }
                });
    }

    public interface PostDataSource {
        Single<List<Post>> getAllPosts();
    }
}
