package uk.co.jbarat.adorablelist.main;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import uk.co.jbarat.domain.post.Post;
import uk.co.jbarat.domain.post.PostUseCase;
import uk.co.jbarat.domain.user.User;
import uk.co.jbarat.domain.user.UserUseCase;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    private static final int USER_ID = 23;
    private static final int USER_ID_2 = 2;

    private static final int POST_ID = 1;
    private static final int POST_ID_2 = 2;
    private static final int POST_ID_3 = 3;

    private static final User TOM = new User(USER_ID, "Tom", "tom@fdgdf.gy");
    private static final User JERRY = new User(USER_ID_2, "Jerry", "jerry@fdgdf.gy");

    private static final Map<Integer, User> USERS = new HashMap<Integer, User>() {
        {
            put(USER_ID, TOM);
            put(USER_ID_2, JERRY);
        }
    };
    private static final List<Post> POSTS = asList(
            new Post(POST_ID, USER_ID, "title", "body"),
            new Post(POST_ID_2, USER_ID, "title", "body"),
            new Post(POST_ID_3, USER_ID_2, "title", "body")
    );

    private static final List<Post> POSTS_EMPTY = emptyList();

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private UserUseCase userUseCase;
    @Mock private PostUseCase postUseCase;
    @Mock private MainView mainView;

    @Captor private ArgumentCaptor<List<ListViewModel>> listArgumentCaptor;

    private PublishSubject<ListViewModel> selection = PublishSubject.create();
    private PublishSubject<Object> retry = PublishSubject.create();

    @Test
    public void shouldSetViewToProgress_whenAttached() {
        MainPresenter mainPresenter = givenAMainPresenter();

        mainPresenter.attach(mainView, selection, retry);

        verify(mainView).setToProgress();
    }

    @Test
    public void shouldCollectCorrectData_whenAttached() {
        MainPresenter mainPresenter = givenAMainPresenter();

        mainPresenter.attach(mainView, selection, retry);

        verify(mainView).updatePostsList(listArgumentCaptor.capture());
        verifyCorrectData(listArgumentCaptor.getValue());
    }

    @Test
    public void shouldCollectCorrectData_whenAttachedAndRetryClicked() {
        MainPresenter mainPresenter = givenAMainPresenter();

        mainPresenter.attach(mainView, selection, retry);
        retry.onNext(new Object());

        verify(mainView, times(2)).updatePostsList(listArgumentCaptor.capture());
        verifyCorrectData(listArgumentCaptor.getValue());
    }

    @Test
    public void shouldUpdateViewWithEmptyList_whenNoPostsReturned() {
        MainPresenter mainPresenter = givenAMainPresenterWithEmptyPosts();

        mainPresenter.attach(mainView, selection, retry);

        verify(mainView).updatePostsList(listArgumentCaptor.capture());
    }

    @Test
    public void shouldNotifyView_whenPostSelected() {
        MainPresenter mainPresenter = givenAMainPresenter();

        mainPresenter.attach(mainView, selection, retry);
        selection.onNext(new ListViewModel(POST_ID, "", ""));

        verify(mainView).startPostDetailsActivity(POST_ID);
    }

    @Test
    public void shouldNotUpdateView_afterDeAttached() {
        MainPresenter mainPresenter = givenAMainPresenter();

        mainPresenter.attach(mainView, selection, retry);
        mainPresenter.deAttach();

        retry.onNext(new Object());
        retry.onNext(new Object());
        retry.onNext(new Object());

        verify(mainView).setToProgress();
    }

    private void verifyCorrectData(List<ListViewModel> value) {
        for (int i = 0; i < value.size(); i++) {
            ListViewModel listViewModel = value.get(i);

            assertThat(listViewModel.getId()).isEqualTo(POSTS.get(i).getId());
            assertThat(listViewModel.getTitle()).isEqualTo(POSTS.get(i).getTitle());
            assertThat(listViewModel.getUserEmail()).isEqualTo(USERS.get(POSTS.get(i).getUserId()).getEmail());
        }
    }

    private MainPresenter givenAMainPresenterWithEmptyPosts() {
        when(postUseCase.getAllPosts()).thenReturn(Single.just(POSTS_EMPTY));

        return new MainPresenter(postUseCase, userUseCase, Schedulers.trampoline());
    }

    private MainPresenter givenAMainPresenter() {
        when(postUseCase.getAllPosts()).thenReturn(Single.just(POSTS));
        when(userUseCase.getUser(USER_ID)).thenReturn(Single.just(TOM));
        when(userUseCase.getUser(USER_ID_2)).thenReturn(Single.just(JERRY));

        return new MainPresenter(postUseCase, userUseCase, Schedulers.trampoline());
    }
}