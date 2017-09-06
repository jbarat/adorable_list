package uk.co.jbarat.data.user;

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
import uk.co.jbarat.domain.logger.Logger;
import uk.co.jbarat.domain.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.jbarat.data.NetworkConstants.LOG_HTTP_CODE;

@SuppressWarnings("ThrowableInstanceNeverThrown")
public class UserRepositoryTest {

    private static final IOException NETWORK_FAILURE_EXCEPTION = new IOException();
    private static final int HTTP_RESPONSE_NOT_FOUND = 404;

    private static final UserResponse USER_1 = new UserResponse(1, "Fred", "Fred?", "fred@ty.com", null, null, null, null);
    private static final UserResponse USER_2 = new UserResponse(2, "John", "John?", "John@ty.com", null, null, null, null);
    private static final UserResponse USER_3 = new UserResponse(3, "Greg", "Greg?", "Greg@ty.com", null, null, null, null);

    private static final List<UserResponse> SUCCESSFUL_RESPONSE = Arrays.asList(
            USER_1,
            USER_2,
            USER_3
    );

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private UserWebService userWebService;
    @Mock private Logger logger;

    @Test
    public void shouldTransformDataLayerResultToDomainLayer_whenCorrectNetworkResultReceived() {
        UserRepository userRepository = givenAUserRepository();

        TestObserver<List<User>> testObserver = userRepository.getAllUsers().test();

        verifySuccessResult(testObserver);
    }

    @Test
    public void shouldGetEmptyResult_whenIoException() {
        UserRepository postRepository = givenAUserRepositoryWithIoException();

        TestObserver<List<User>> testObserver = postRepository.getAllUsers().test();

        assertThat(testObserver.valueCount()).isEqualTo(1);
        assertThat(testObserver.values().get(0).size()).isEqualTo(0);
    }

    @Test
    public void shouldGetEmptyResult_whenResponseHttpCodeIs404() {
        UserRepository postRepository = givenAUserRepositoryWith404Response();

        TestObserver<List<User>> testObserver = postRepository.getAllUsers().test();

        assertThat(testObserver.valueCount()).isEqualTo(1);
        assertThat(testObserver.values().get(0).size()).isEqualTo(0);
    }

    @Test
    public void shouldLogException_whenIoException() {
        UserRepository postRepository = givenAUserRepositoryWithIoException();

        postRepository.getAllUsers().test();

        verify(logger).logThrowable(NETWORK_FAILURE_EXCEPTION);
    }

    @Test
    public void shouldLogInfo_whenNon200ResponseReceived() {
        UserRepository postRepository = givenAUserRepositoryWith404Response();

        postRepository.getAllUsers().test();

        verify(logger).logMessage(LOG_HTTP_CODE + HTTP_RESPONSE_NOT_FOUND);
    }

    private void verifySuccessResult(TestObserver<List<User>> testObserver) {
        assertThat(testObserver.valueCount()).isEqualTo(1);

        List<User> get = testObserver.values().get(0);
        for (int i = 0; i < get.size(); i++) {
            User user = get.get(i);
            assertThat(user.getId()).isEqualTo(SUCCESSFUL_RESPONSE.get(i).getId());
            assertThat(user.getName()).isEqualTo(SUCCESSFUL_RESPONSE.get(i).getName());
            assertThat(user.getEmail()).isEqualTo(SUCCESSFUL_RESPONSE.get(i).getEmail());
        }
    }

    private UserRepository givenAUserRepositoryWith404Response() {
        when(userWebService.getUsers()).thenReturn(Single.just(
                Result.response(Response.error(HTTP_RESPONSE_NOT_FOUND, new NoContentResponseBody()))));
        return new UserRepository(userWebService, logger);
    }

    private UserRepository givenAUserRepositoryWithIoException() {
        when(userWebService.getUsers()).thenReturn(Single.just(
                Result.error(NETWORK_FAILURE_EXCEPTION)));
        return new UserRepository(userWebService, logger);
    }

    private UserRepository givenAUserRepository() {
        when(userWebService.getUsers()).thenReturn(Single.just(
                Result.response(Response.success(SUCCESSFUL_RESPONSE))));
        return new UserRepository(userWebService, logger);
    }
}