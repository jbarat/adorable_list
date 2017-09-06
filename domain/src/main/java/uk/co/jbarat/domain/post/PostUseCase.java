package uk.co.jbarat.domain.post;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import uk.co.jbarat.domain.scope.Activity;

public class PostUseCase {

    private final Scheduler scheduler;
    private final Single<List<Post>> cashedDataSource;

    @Inject
    PostUseCase(PostDataSource postDataSource, @Named("io") Scheduler scheduler) {
        this.scheduler = scheduler;

        cashedDataSource = postDataSource.getAllPosts().cache();
    }

    public Observable<Post> getAllPosts() {
        return cashedDataSource
                .flattenAsObservable(posts -> posts)
                .subscribeOn(scheduler);
    }

    public interface PostDataSource {
        Single<List<Post>> getAllPosts();
    }
}
