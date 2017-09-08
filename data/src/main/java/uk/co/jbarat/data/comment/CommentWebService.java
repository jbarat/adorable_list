package uk.co.jbarat.data.comment;

import java.util.List;

import io.reactivex.Single;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommentWebService {

    @GET("comments")
    Single<Result<List<CommentResponse>>> getComments(@Query("postId") int postId);
}
