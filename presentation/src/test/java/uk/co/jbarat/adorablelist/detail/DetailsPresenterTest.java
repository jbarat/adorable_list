package uk.co.jbarat.adorablelist.detail;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import uk.co.jbarat.domain.comment.CommentUseCase;
import uk.co.jbarat.domain.post.Post;
import uk.co.jbarat.domain.post.PostUseCase;
import uk.co.jbarat.domain.user.User;
import uk.co.jbarat.domain.user.UserUseCase;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailsPresenterTest {

    private static final int USER_ID = 12;
    private static final int POST_ID = 546;

    private static final Post POST = new Post(POST_ID, USER_ID, "The life of Tom the cat.", "It's a sad story");
    private static final User TOM = new User(USER_ID, "Tom", "tom@gfd.d");
    private static final int NUMBER_OF_COMMENTS = 78;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private UserUseCase userUseCase;
    @Mock private PostUseCase postUseCase;
    @Mock private CommentUseCase commentUseCase;
    @Mock private DetailsView detailsView;

    @Captor private ArgumentCaptor<DetailViewModel> detailViewModelArgumentCaptor;

    @Test
    public void shouldCollectTheDetailsOfThePostAndUpdateTheUi_whenAttached() {
        DetailsPresenter detailsPresenter = givenADetailsPresenter();

        detailsPresenter.attach(detailsView, POST_ID);

        verify(detailsView).updateDetails(detailViewModelArgumentCaptor.capture());
        verifyCorrectData(detailViewModelArgumentCaptor.getValue());
    }

    private void verifyCorrectData(DetailViewModel value) {
        assertThat(value.getTitle()).isEqualTo(POST.getTitle());
        assertThat(value.getBody()).isEqualTo(POST.getBody());
        assertThat(value.getComments()).isEqualTo(NUMBER_OF_COMMENTS);
        assertThat(value.getName()).isEqualTo(TOM.getName());
        assertThat(value.getEmail()).isEqualTo(TOM.getEmail());
    }

    private DetailsPresenter givenADetailsPresenter() {
        when(userUseCase.getUser(USER_ID)).thenReturn(Single.just(TOM));
        when(postUseCase.getPost(POST_ID)).thenReturn(Single.just(POST));
        when(commentUseCase.getNumberOfCommentOfPost(POST_ID)).thenReturn(Single.just(NUMBER_OF_COMMENTS));

        return new DetailsPresenter(userUseCase, postUseCase, commentUseCase, Schedulers.trampoline());
    }
}