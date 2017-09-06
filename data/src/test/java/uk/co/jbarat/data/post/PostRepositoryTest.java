package uk.co.jbarat.data.post;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import uk.co.jbarat.data.NoContentResponseBody;
import uk.co.jbarat.domain.logger.Logger;
import uk.co.jbarat.domain.post.Post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.jbarat.data.NetworkConstants.LOG_HTTP_CODE;

@SuppressWarnings("ThrowableInstanceNeverThrown")
public class PostRepositoryTest {

    private static final IOException NETWORK_FAILURE_EXCEPTION = new IOException();
    private static final int HTTP_RESPONSE_NOT_FOUND = 404;

    private static final PostResponse POST_1 = new PostResponse(1, 1, "Title", "Body");
    private static final PostResponse POST_2 = new PostResponse(2, 1, "Title2", "Body2");
    private static final PostResponse POST_3 = new PostResponse(3, 1, "Title3", "Body3");

    private static final List<PostResponse> SUCCESSFUL_RESPONSE = Arrays.asList(
            POST_1,
            POST_2,
            POST_3
    );

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private PostWebService postWebService;
    @Mock private Logger logger;

    @Test
    public void shouldTransformDataLayerResultToDomainLayer_whenCorrectNetworkResultReceived() {
        PostRepository postRepository = givenAPostRepository();

        TestObserver<List<Post>> testObserver = postRepository.getAllPosts().test();

        verifySuccessResult(testObserver);
    }

    @Test
    public void shouldGetEmptyResult_whenIoException() {
        PostRepository postRepository = givenAPostRepositoryWithIoException();

        TestObserver<List<Post>> testObserver = postRepository.getAllPosts().test();

        assertThat(testObserver.valueCount()).isEqualTo(1);
        assertThat(testObserver.values().get(0).size()).isEqualTo(0);
    }

    @Test
    public void shouldGetEmptyResult_whenResponseHttpCodeIs404() {
        PostRepository postRepository = givenAPostRepositoryWith404Response();

        TestObserver<List<Post>> testObserver = postRepository.getAllPosts().test();

        assertThat(testObserver.valueCount()).isEqualTo(1);
        assertThat(testObserver.values().get(0).size()).isEqualTo(0);
    }

    @Test
    public void shouldLogException_whenIoException() {
        PostRepository postRepository = givenAPostRepositoryWithIoException();

        postRepository.getAllPosts().test();

        verify(logger).logThrowable(NETWORK_FAILURE_EXCEPTION);
    }

    @Test
    public void shouldLogInfo_whenNon200ResponseReceived() {
        PostRepository postRepository = givenAPostRepositoryWith404Response();

        postRepository.getAllPosts().test();

        verify(logger).logMessage(LOG_HTTP_CODE + HTTP_RESPONSE_NOT_FOUND);
    }

    private void verifySuccessResult(TestObserver<List<Post>> testObserver) {
        assertThat(testObserver.valueCount()).isEqualTo(1);

        List<Post> get = testObserver.values().get(0);
        for (int i = 0; i < get.size(); i++) {
            Post post = get.get(i);
            assertThat(post.getId()).isEqualTo(SUCCESSFUL_RESPONSE.get(i).getId());
            assertThat(post.getTitle()).isEqualTo(SUCCESSFUL_RESPONSE.get(i).getTitle());
            assertThat(post.getBody()).isEqualTo(SUCCESSFUL_RESPONSE.get(i).getBody());
            assertThat(post.getUserId()).isEqualTo(SUCCESSFUL_RESPONSE.get(i).getUserId());
        }
    }

    private PostRepository givenAPostRepositoryWith404Response() {
        when(postWebService.getPosts()).thenReturn(Single.just(
                Result.response(Response.error(HTTP_RESPONSE_NOT_FOUND, new NoContentResponseBody()))));
        return new PostRepository(postWebService, logger);
    }

    private PostRepository givenAPostRepositoryWithIoException() {
        when(postWebService.getPosts()).thenReturn(Single.just(
                Result.error(NETWORK_FAILURE_EXCEPTION)));
        return new PostRepository(postWebService, logger);
    }

    private PostRepository givenAPostRepository() {
        when(postWebService.getPosts()).thenReturn(Single.just(
                Result.response(Response.success(SUCCESSFUL_RESPONSE))));
        return new PostRepository(postWebService, logger);
    }
}