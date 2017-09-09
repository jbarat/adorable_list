package uk.co.jbarat.domain.comment;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class CommentUseCaseTest {

    private static final int POST_ID = 23;

    private static final List<Comment> COMMENTS = asList(
            new Comment(1),
            new Comment(2),
            new Comment(3)
    );

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private CommentUseCase.CommentDataSource commentDataSource;

    @Test
    public void shouldCountTheComments_whenResultReceived() {
        CommentUseCase commentUseCase = givenACommentUseCase();

        TestObserver<Integer> observer = commentUseCase.getNumberOfCommentOfPost(POST_ID).test();

        assertThat(observer.values().size()).isEqualTo(1);
        assertThat(observer.values().get(0)).isEqualTo(COMMENTS.size());
    }

    private CommentUseCase givenACommentUseCase() {
        when(commentDataSource.getComments(POST_ID)).thenReturn(Single.just(COMMENTS));

        return new CommentUseCase(commentDataSource);
    }

}