package uk.co.jbarat.domain.post;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostUseCaseTest {

    private static final Post POST = new Post(1, 1, "", "");
    private static final Post POST1 = new Post(2, 1, "", "");
    private static final Post POST2 = new Post(3, 1, "", "");

    private static final List<Post> POST_LIST = asList(
            POST,
            POST1,
            POST2);

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private PostUseCase.PostDataSource postDataSource;

    private int counter;

    @Test
    public void shouldGetAllThePosts_whenRequested() {
        PostUseCase postUseCase = givenAPostUseCase();

        TestObserver<List<Post>> testObserver = postUseCase.getAllPosts().test();

        testObserver.assertComplete();
        assertThat(testObserver.values().get(0)).isEqualTo(POST_LIST);
    }

    @Test
    public void shouldGetCachedResponse_whenGetAllCalledMultipleTimes(){
        PostUseCase postUseCase = givenAPostUseCaseWithACounter();

        postUseCase.getAllPosts().test();
        postUseCase.getAllPosts().test();
        postUseCase.getAllPosts().test();
        TestObserver<List<Post>> testObserver = postUseCase.getAllPosts().test();

        assertThat(testObserver.valueCount()).isEqualTo(1);
        assertThat(testObserver.values().get(0)).isEqualTo(POST_LIST);
        assertThat(counter).isEqualTo(1);
    }

    @Test
    public void shouldReturnEmpty_whenNoCacheAndNoDataSourceReturnsData(){
        PostUseCase postUseCase = givenAPostUseCaseWithoutData();

        TestObserver<List<Post>> testObserver = postUseCase.getAllPosts().test();

        testObserver.assertComplete();
        assertThat(testObserver.values().get(0)).isEqualTo(emptyList());
    }

    @Test
    public void shouldGetCorrectPost_whenValidIdProvided(){
        PostUseCase postUseCase = givenAPostUseCase();

        TestObserver<Post> testObserver = postUseCase.getPost(POST.getId()).test();

        assertThat(testObserver.valueCount()).isEqualTo(1);
        assertThat(testObserver.values().get(0)).isEqualTo(POST);
    }

    @Test
    public void shouldGetError_whenInvalidIdProvided(){
        PostUseCase postUseCase = givenAPostUseCase();

        TestObserver<Post> testObserver = postUseCase.getPost(3423).test();

        assertThat(testObserver.valueCount()).isEqualTo(0);
        assertThat(testObserver.errorCount()).isEqualTo(1);
        assertThat(testObserver.errors().get(0)).isInstanceOf(NoSuchElementException.class);
    }

    private PostUseCase givenAPostUseCaseWithoutData() {
        when(postDataSource.getAllPosts()).thenReturn(Single.just(emptyList()));
        return new PostUseCase(postDataSource, Schedulers.trampoline());
    }

    private PostUseCase givenAPostUseCaseWithACounter() {
        counter = 0;
        when(postDataSource.getAllPosts()).thenReturn(Single.just(POST_LIST).doOnSuccess(posts -> counter++));
        return new PostUseCase(postDataSource, Schedulers.trampoline());
    }

    @SuppressWarnings("unchecked")
    private PostUseCase givenAPostUseCase() {
        when(postDataSource.getAllPosts()).thenReturn(Single.just(POST_LIST));
        return new PostUseCase(postDataSource, Schedulers.trampoline());
    }
}