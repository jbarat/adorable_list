package uk.co.jbarat.data.post;

import java.util.List;

import io.reactivex.Single;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;

public interface PostWebService {

    @GET("posts")
    Single<Result<List<PostResponse>>> getPosts();
}
