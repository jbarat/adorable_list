package uk.co.jbarat.data.comment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.adapter.rxjava2.Result;
import uk.co.jbarat.domain.comment.Comment;
import uk.co.jbarat.domain.comment.CommentUseCase;
import uk.co.jbarat.domain.logger.Logger;

import static uk.co.jbarat.data.NetworkConstants.LOG_HTTP_CODE;

/**
 * Simple retrofit web service implementation. If there is a problem with the call it will return an
 * empty list.
 */
public class CommentRepository implements CommentUseCase.CommentDataSource {
    private final CommentWebService commentWebService;
    private final Logger logger;

    @Inject
    CommentRepository(CommentWebService webService, Logger logger) {
        this.commentWebService = webService;
        this.logger = logger;
    }

    @Override
    public Single<List<Comment>> getComments(int postId) {
        return commentWebService.getComments(postId)
                .map(this::processResponse);
    }

    /**
     * Transform the web service response into a domain layer object.
     */
    @SuppressWarnings("ConstantConditions")
    private List<Comment> processResponse(Result<List<CommentResponse>> result) {
        List<Comment> comments = new ArrayList<>();

        if (result.isError()) {
            logger.logThrowable(result.error());
        } else if (!result.response().isSuccessful()) {
            logger.logMessage(LOG_HTTP_CODE + result.response().code());
        } else if (result.response().isSuccessful()) {
            List<CommentResponse> responses = result.response().body();

            for (CommentResponse response : responses) {
                comments.add(new Comment(response.getId()));
            }
        }

        return comments;
    }
}
