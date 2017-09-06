package uk.co.jbarat.data.post;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.adapter.rxjava2.Result;
import uk.co.jbarat.domain.logger.Logger;
import uk.co.jbarat.domain.post.Post;
import uk.co.jbarat.domain.post.PostUseCase;

import static uk.co.jbarat.data.NetworkConstants.LOG_HTTP_CODE;

public class PostRepository implements PostUseCase.PostDataSource {

    private final PostWebService postWebService;
    private final Logger logger;

    @Inject
    PostRepository(PostWebService postWebService, Logger logger) {
        this.postWebService = postWebService;
        this.logger = logger;
    }

    @Override
    public Single<List<Post>> getAllPosts() {
        return postWebService.getPosts()
                .map(this::processResponse);
    }

    /**
     * Transform the web service response into a domain layer object.
     */
    @SuppressWarnings("ConstantConditions")
    private List<Post> processResponse(Result<List<PostResponse>> result) {
        List<Post> posts = new ArrayList<>();

        if (result.isError()) {
            logger.logThrowable(result.error());
        } else if (!result.response().isSuccessful()) {
            logger.logMessage(LOG_HTTP_CODE + result.response().code());
        } else if (result.response().isSuccessful()) {
            List<PostResponse> responses = result.response().body();

            for (PostResponse response : responses) {
                posts.add(new Post(response.getId(), response.getUserId(), response.getTitle(), response.getBody()));
            }
        }

        return posts;
    }
}
