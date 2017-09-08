package uk.co.jbarat.domain.post;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import uk.co.jbarat.domain.scope.Activity;

@Activity
public class PostUseCase {

    private final Scheduler scheduler;
    private final Single<List<Post>> cashedDataSource;

    @Inject
    PostUseCase(PostDataSource postDataSource, @Named("io") Scheduler scheduler) {
        this.scheduler = scheduler;

        cashedDataSource = postDataSource.getAllPosts()
                .flatMap(this::cache);
    }

    private SingleSource<? extends List<Post>> cache(List<Post> posts) {
        if(posts.isEmpty()){
           return Single.just(posts);
        }else{
            return Single.just(posts).cache();
        }
    }

    public Single<List<Post>> getAllPosts() {
        return cashedDataSource
                .subscribeOn(scheduler);
    }

    public Observable<Post> getPost(int postId) {
        return getAllPosts()
                .flattenAsObservable(posts -> posts)
                .filter(post -> post.getId() == postId);
    }

    public interface PostDataSource {
        Single<List<Post>> getAllPosts();
    }
}
