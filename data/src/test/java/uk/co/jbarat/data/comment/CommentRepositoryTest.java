package uk.co.jbarat.data.comment;

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
import uk.co.jbarat.domain.comment.Comment;
import uk.co.jbarat.domain.logger.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.jbarat.data.NetworkConstants.LOG_HTTP_CODE;

@SuppressWarnings("ThrowableInstanceNeverThrown")
public class CommentRepositoryTest {
    private static final IOException NETWORK_FAILURE_EXCEPTION = new IOException();
    private static final int HTTP_RESPONSE_NOT_FOUND = 404;

    private static final CommentResponse COMMENT_1 = new CommentResponse(1, 1, "Title", "email", "Body");
    private static final CommentResponse COMMENT_2 = new CommentResponse(2, 1, "Title2", "email", "Body2");
    private static final CommentResponse COMMENT_3 = new CommentResponse(3, 1, "Title3", "email", "Body3");

    private static final List<CommentResponse> SUCCESSFUL_RESPONSE = Arrays.asList(
            COMMENT_1,
            COMMENT_2,
            COMMENT_3
    );
    private static final int POST_ID = 12;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private CommentWebService commentWebService;
    @Mock private Logger logger;

    @Test
    public void shouldTransformDataLayerResultToDomainLayer_whenCorrectNetworkResultReceived() {
        CommentRepository commentRepository = givenACommentRepository();

        TestObserver<List<Comment>> testObserver = commentRepository.getComments(POST_ID).test();

        verifySuccessResult(testObserver);
    }

    @Test
    public void shouldGetEmptyResult_whenIoException() {
        CommentRepository commentRepository = givenACommentRepositoryWithIoException();

        TestObserver<List<Comment>> testObserver = commentRepository.getComments(POST_ID).test();

        assertThat(testObserver.valueCount()).isEqualTo(1);
        assertThat(testObserver.values().get(0).size()).isEqualTo(0);
    }

    @Test
    public void shouldGetEmptyResult_whenResponseHttpCodeIs404() {
        CommentRepository commentRepository = givenACommentRepositoryWith404Response();

        TestObserver<List<Comment>> testObserver = commentRepository.getComments(POST_ID).test();

        assertThat(testObserver.valueCount()).isEqualTo(1);
        assertThat(testObserver.values().get(0).size()).isEqualTo(0);
    }

    @Test
    public void shouldLogException_whenIoException() {
        CommentRepository commentRepository = givenACommentRepositoryWithIoException();

        commentRepository.getComments(POST_ID).test();

        verify(logger).logThrowable(NETWORK_FAILURE_EXCEPTION);
    }

    @Test
    public void shouldLogInfo_whenNon200ResponseReceived() {
        CommentRepository postRepository = givenACommentRepositoryWith404Response();

        postRepository.getComments(POST_ID).test();

        verify(logger).logMessage(LOG_HTTP_CODE + HTTP_RESPONSE_NOT_FOUND);
    }

    private void verifySuccessResult(TestObserver<List<Comment>> testObserver) {
        assertThat(testObserver.valueCount()).isEqualTo(1);

        List<Comment> get = testObserver.values().get(0);
        for (int i = 0; i < get.size(); i++) {
            Comment comment = get.get(i);
            assertThat(comment.getId()).isEqualTo(SUCCESSFUL_RESPONSE.get(i).getId());
        }
    }

    private CommentRepository givenACommentRepositoryWith404Response() {
        when(commentWebService.getComments(POST_ID)).thenReturn(Single.just(
                Result.response(Response.error(HTTP_RESPONSE_NOT_FOUND, new NoContentResponseBody()))));
        return new CommentRepository(commentWebService, logger);
    }

    private CommentRepository givenACommentRepositoryWithIoException() {
        when(commentWebService.getComments(POST_ID)).thenReturn(Single.just(
                Result.error(NETWORK_FAILURE_EXCEPTION)));
        return new CommentRepository(commentWebService, logger);
    }

    private CommentRepository givenACommentRepository() {
        when(commentWebService.getComments(POST_ID)).thenReturn(Single.just(
                Result.response(Response.success(SUCCESSFUL_RESPONSE))));
        return new CommentRepository(commentWebService, logger);
    }
}