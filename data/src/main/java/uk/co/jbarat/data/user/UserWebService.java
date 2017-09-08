package uk.co.jbarat.data.user;

import java.util.List;

import io.reactivex.Single;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import uk.co.jbarat.data.user.response.UserResponse;

public interface UserWebService {

    @GET("users")
    Single<Result<List<UserResponse>>> getUsers();
}
