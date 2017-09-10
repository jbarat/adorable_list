package uk.co.jbarat.adorablelist.main;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import uk.co.jbarat.domain.post.Post;
import uk.co.jbarat.domain.post.PostUseCase;
import uk.co.jbarat.domain.scope.Activity;
import uk.co.jbarat.domain.user.UserUseCase;

import static java.util.Collections.emptyList;

@Activity
class MainPresenter {

    private static final List<ListViewModel> EMPTY_RESULT = emptyList();

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

    void attach(MainView mainView, Observable<ListViewModel> postSelection, Observable<Object> retryClicks) {
        this.mainView = mainView;

        compositeDisposable = new CompositeDisposable();

        //This stream starts as soon it is subscribed and when the retry button clicked
        compositeDisposable.add(Observable.merge(Observable.just(new Object()), retryClicks)
                .doOnNext(o -> mainView.setToProgress())
                .flatMap(ignored -> postUseCase.getAllPosts().toObservable())
                .flatMap(this::getUserDetails)
                .observeOn(scheduler) // jump to the main thread before ui update
                .subscribe(this::updateList));

        // Starts the details activity when item selected
        compositeDisposable.add(postSelection
                .map(ListViewModel::getId)
                .subscribe(this::startDetailsActivity));
    }

    void deAttach() {
        mainView = null;
        compositeDisposable.dispose();
    }

    private void startDetailsActivity(int postId) {
        mainView.startPostDetailsActivity(postId);
    }

    private void updateList(List<ListViewModel> posts) {
        if (mainView != null) {
            mainView.updatePostsList(posts);
        }
    }

    private ObservableSource<List<ListViewModel>> getUserDetails(List<Post> posts) {
        if (posts.isEmpty()) {
            return Observable.just(EMPTY_RESULT);
        } else {
            return Observable.just(posts)
                    .flatMapIterable(post -> post) // Explode the list to individual items to make processing easier
                    .flatMap(post -> userUseCase.getUser(post.getUserId())
                            .flatMapObservable(user -> Observable.just(user.getEmail()))
                            .map(userEmail -> new ListViewModel(post.getId(), post.getTitle(), userEmail)))
                    .toSortedList()// Collect the list items into one list
                    .toObservable();
        }
    }

}
