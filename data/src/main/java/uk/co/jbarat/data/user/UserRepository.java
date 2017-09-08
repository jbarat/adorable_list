package uk.co.jbarat.data.user;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.adapter.rxjava2.Result;
import uk.co.jbarat.data.user.response.UserResponse;
import uk.co.jbarat.domain.logger.Logger;
import uk.co.jbarat.domain.user.User;
import uk.co.jbarat.domain.user.UserUseCase;

import static uk.co.jbarat.data.NetworkConstants.LOG_HTTP_CODE;

public class UserRepository implements UserUseCase.UserDataSource {

    private final UserWebService userWebService;
    private final Logger logger;

    @Inject
    UserRepository(UserWebService userWebService, Logger logger) {
        this.userWebService = userWebService;
        this.logger = logger;
    }

    @Override
    public Single<List<User>> getAllUsers() {
        return userWebService.getUsers()
                .map(this::processResponse);
    }

    /**
     * Transform the web service response into a domain layer object.
     */
    @SuppressWarnings("ConstantConditions")
    private List<User> processResponse(Result<List<UserResponse>> result) {
        List<User> users = new ArrayList<>();

        if (result.isError()) {
            logger.logThrowable(result.error());
        } else if (!result.response().isSuccessful()) {
            logger.logMessage(LOG_HTTP_CODE + result.response().code());
        } else if (result.response().isSuccessful()) {
            List<UserResponse> responses = result.response().body();

            for (UserResponse response : responses) {
                users.add(new User(response.getId(), response.getName(), response.getEmail()));
            }
        }

        return users;
    }
}
