package uk.co.jbarat.domain.post;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class PostUseCaseTest {

    private static final List<Post> POST_LIST = asList(
            new Post(1, 1, "", ""),
            new Post(2, 1, "", ""),
            new Post(3, 1, "", ""));

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private PostUseCase.PostDataSource postDataSource;

    @Test
    public void shouldGetAllThePosts_whenRequested() {
        PostUseCase postUseCase = givenAPostUseCase();

        TestObserver<List<Post>> testObserver = postUseCase.getAllPosts().test();

        testObserver.assertComplete();
        assertThat(testObserver.values()).isEqualTo(POST_LIST);
    }

    @SuppressWarnings("unchecked")
    private PostUseCase givenAPostUseCase() {
        when(postDataSource.getAllPosts()).thenReturn(Single.just(POST_LIST));
        return new PostUseCase(postDataSource, Schedulers.trampoline());
    }
}