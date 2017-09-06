package uk.co.jbarat.data.comment;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;

public interface CommentWebService {

    @GET("comments")
    Observable<Result<List<Comment>>> getComments();
}
