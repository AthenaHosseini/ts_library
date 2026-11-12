package se.yrgo.libraryapp.controllers;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import se.yrgo.libraryapp.services.UserService;
import se.yrgo.libraryapp.dao.*;
import se.yrgo.libraryapp.entities.*;
import se.yrgo.libraryapp.entities.forms.*;
import static org.assertj.core.api.Assertions.assertThat;
import io.jooby.Context;
import io.jooby.StatusCode;
import java.util.*;
import io.jooby.Cookie;

public class LoginControllerTest {
    @Test
    void testNoSession() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);
        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        assertThat(controller.isLoggedIn(null)).isEmpty();
    }

    @Test
    void testInvalidSession() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);
        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        assertThat(controller.isLoggedIn("fel")).isEmpty();
    }

    @Test
    void testWrongLogin() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);
        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        LoginData login = new LoginData("test", "fel");
        Context context = mock(Context.class);
        when(userService.validate("test", "fel")).thenReturn(Optional.empty());
        assertThat(controller.login(context, null, login)).isEmpty();
        verify(context).setResponseCode(StatusCode.UNAUTHORIZED);
    }

    @Test
    void testCorrectLogin() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);
        LoginController controller = new LoginController(userService, roleDao, sessionDao);
        LoginData login = new LoginData("test", "ratt");
        Context context = mock(Context.class);
        UserId userId = mock(UserId.class);

        when(userService.validate("test", "ratt")).thenReturn(Optional.of(userId));

        UUID sessionId = UUID.randomUUID();
        when(sessionDao.create(userId)).thenReturn(sessionId);

        when(roleDao.get(userId)).thenReturn(List.of(Role.USER));
        assertThat(controller.login(context, null, login)).contains(Role.USER);

        verify(userService).validate("test", "ratt");
        verify(sessionDao).create(userId);
        verify(roleDao).get(userId);
        verify(context).setResponseCookie(any(Cookie.class));
    }

    @Test
    void testValidSession() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);

        LoginController controller = new LoginController(userService, roleDao, sessionDao);

        UserId userId = mock(UserId.class);
        UUID sessionId = UUID.randomUUID();

        when(sessionDao.validate(sessionId)).thenReturn(userId);
        when(roleDao.get(userId)).thenReturn(List.of(Role.USER));

        assertThat(controller.isLoggedIn(sessionId.toString())).contains(Role.USER);

        verify(sessionDao).validate(sessionId);
        verify(roleDao).get(userId);
    }

    @Test
    void testAlreadyLoggedIn() {
        UserService userService = mock(UserService.class);
        RoleDao roleDao = mock(RoleDao.class);
        SessionDao sessionDao = mock(SessionDao.class);

        LoginController controller = new LoginController(userService, roleDao, sessionDao);

        LoginData login = new LoginData("test", "ratt");
        Context context = mock(Context.class);

        UserId userId = mock(UserId.class);
        UUID sessionId = UUID.randomUUID();

        when(sessionDao.validate(sessionId)).thenReturn(userId);

        assertThat(controller.login(context, sessionId.toString(), login)).isEmpty();

        verify(sessionDao).validate(sessionId);
        verify(userService, never()).validate("test", "ratt");
    }

}
