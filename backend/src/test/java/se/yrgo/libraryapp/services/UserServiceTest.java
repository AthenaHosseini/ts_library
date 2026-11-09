package se.yrgo.libraryapp.services;

import org.mockito.junit.jupiter.MockitoSettings;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.quality.Strictness;

import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.*;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @Test
    @SuppressWarnings("deprecation")
    void correctLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash = "password";

        final LoginInfo info = new LoginInfo(id, passwordHash);

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));

        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.of(id));
    }

    @Test
    void nameIsAvailable() {
        final String username = "testuser";

        when(userDao.isNameAvailable(username)).thenReturn(true);
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.isNameAvailable(username)).isTrue();
    }

    @Test
    void nameIsNotAvailable() {
        final String username = "testuser";

        when(userDao.isNameAvailable(username)).thenReturn(false);

        final PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);

        assertThat(userService.isNameAvailable(username)).isFalse();
    }

    @Test
    void registerUser() {
        final String username = "testuser";
        final String realname = "Test user";
        final String password = "hej";

        when(userDao.register(username, realname, password)).thenReturn(true);
        final PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.register(username, realname, password)).isTrue();
    }

    @Test
    void nameIsTooShort() {
        final String username = "hi";
        final PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.isNameAvailable(username)).isFalse();
    }
}
