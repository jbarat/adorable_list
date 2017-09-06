package uk.co.jbarat.domain.user;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserUseCaseTest {

    private static final User USER_1 = new User(1, "Jack", "jack@fsdg.cvb");
    private static final User USER_2 = new User(2, "John", "john@fsdg.cvb");

    private static final List<User> USER_LIST = Arrays.asList(USER_1, USER_2);

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private UserUseCase.UserDataSource userDataSource;

    @Test
    public void shouldGetCorrectUser_whenIdRequested() {
        UserUseCase userUseCase = givenAUserUseCase();

        User user = userUseCase.getUser(USER_2.getId()).blockingGet();

        assertThat(user).isEqualTo(USER_2);
    }

    @Test
    public void shouldNotCallDataSourceMoreThenOnce_whenCalledMultipleTimes() {
        UserUseCase userUseCase = givenAUserUseCase();

        userUseCase.getUser(USER_2.getId()).blockingGet();
        userUseCase.getUser(USER_2.getId()).blockingGet();
        userUseCase.getUser(USER_2.getId()).blockingGet();

        verify(userDataSource, times(1)).getAllUsers();
    }

    private UserUseCase givenAUserUseCase() {
        when(userDataSource.getAllUsers()).thenReturn(Single.just(USER_LIST));

        return new UserUseCase(userDataSource, Schedulers.trampoline());
    }

}