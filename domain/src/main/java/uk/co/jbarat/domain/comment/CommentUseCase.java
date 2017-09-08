package uk.co.jbarat.domain.comment;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import uk.co.jbarat.domain.scope.Activity;

@Activity
public class CommentUseCase {

    private final CommentDataSource commentDataSource;

    @Inject
    CommentUseCase(CommentDataSource commentDataSource){
        this.commentDataSource = commentDataSource;
    }

    public Single<Integer> getNumberOfCommentOfPost(int postId){
        return commentDataSource.getComments(postId)
                .map(List::size);
    }

    public interface CommentDataSource {
        Single<List<Comment>> getComments(int postId);
    }
}
