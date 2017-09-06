package uk.co.jbarat.adorablelist.main;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import uk.co.jbarat.domain.post.Post;
import uk.co.jbarat.domain.post.PostUseCase;
import uk.co.jbarat.domain.scope.Activity;
import uk.co.jbarat.domain.user.UserUseCase;

@Activity
class MainPresenter {

    private final PostUseCase postUseCase;
    private final UserUseCase userUseCase;
    private final Scheduler scheduler;

    private CompositeDisposable compositeDisposable;
    private MainView mainView;

    @Inject
    MainPresenter(PostUseCase postUseCase, UserUseCase userUseCase, @Named("main") Scheduler scheduler) {
        this.postUseCase = postUseCase;
        this.userUseCase = userUseCase;
        this.scheduler = scheduler;
    }

    void attach(MainView mainView) {
        this.mainView = mainView;

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(postUseCase.getAllPosts()
                .flatMap(post -> userUseCase.getUser(post.getUserId())
                        .flatMapObservable(user -> Observable.just(user.getEmail()))
                        .map(userEmail -> new PostViewModel(post.getId(), post.getBody(), post.getTitle(), userEmail)))
                .toList()
                .observeOn(scheduler)
                .subscribe(this::updateList));

    }

    private void updateList(List<PostViewModel> posts) {
        if (mainView != null) {
            mainView.updatePostsList(posts);
        }

        postUseCase.getAllPosts()
                .observeOn(scheduler)
                .subscribe();
    }

    void deAttach() {
        mainView = null;
        compositeDisposable.dispose();
    }
}
