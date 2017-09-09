package uk.co.jbarat.adorablelist.detail;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import uk.co.jbarat.domain.comment.CommentUseCase;
import uk.co.jbarat.domain.post.Post;
import uk.co.jbarat.domain.post.PostUseCase;
import uk.co.jbarat.domain.scope.Activity;
import uk.co.jbarat.domain.user.UserUseCase;

@Activity
class DetailsPresenter {

    private final UserUseCase userUseCase;
    private final PostUseCase postUseCase;
    private final CommentUseCase commentUseCase;
    private final Scheduler scheduler;

    private CompositeDisposable compositeDisposable;
    private DetailsView detailsView;

    @Inject
    DetailsPresenter(UserUseCase userUseCase, PostUseCase postUseCase, CommentUseCase commentUseCase,
                     @Named("main")Scheduler scheduler) {
        this.userUseCase = userUseCase;
        this.postUseCase = postUseCase;
        this.commentUseCase = commentUseCase;
        this.scheduler = scheduler;
    }

    void attach(DetailsView detailsView, int postId) {
        this.detailsView = detailsView;
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(postUseCase.getPost(postId)
                .flatMap(this::collectDetails)
                .observeOn(scheduler)
                .subscribe(this::updateView));
    }

    void deAttach() {
        detailsView = null;
        compositeDisposable.dispose();
    }

    private Single<DetailViewModel> collectDetails(Post post) {
        return Single.zip(userUseCase.getUser(post.getUserId()),
                commentUseCase.getNumberOfCommentOfPost(post.getId()),
                (user, comments) -> new DetailViewModel(post.getTitle(), post.getBody(),
                        user.getName(), user.getEmail(), comments));
    }

    private void updateView(DetailViewModel detailViewModel) {
        if (detailsView != null) {
            detailsView.updateDetails(detailViewModel);
        }
    }
}
