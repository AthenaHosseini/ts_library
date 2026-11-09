package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.booleanThat;

import java.util.Optional;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class UserDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        // this way we do not need to create a new datasource every time
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;
    }

    @Test
    void getUserById() {
        // this data comes from the test migration files
        final String username = "test";
        final UserId userId = UserId.of(1);
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }

    @Test
    void getNonExistingUser() {
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get("1234567");
        assertThat(maybeUser).isEmpty();
    }

    @Test
    void nameIsNotAvailable() {
        UserDao userDao = new UserDao(ds);
        boolean available = userDao.isNameAvailable("test");
        assertThat(available).isFalse();
    }

    @Test
    void nameIsAvailable() {
        UserDao userDao = new UserDao(ds);
        boolean available = userDao.isNameAvailable("nyanvandare");
        assertThat(available).isTrue();
    }

    @Test
    void getExistingLoginInfo() {
        UserDao userDao = new UserDao(ds);
        Optional<LoginInfo> maybeLoginInfo = userDao.getLoginInfo("test");
        assertThat(maybeLoginInfo).isPresent();
        assertThat(maybeLoginInfo.get().getUserId()).isEqualTo(UserId.of(1));
        assertThat(maybeLoginInfo.get().getPasswordHash()).isNotEmpty();
    }

    @Test
    void getNonExistingLoginInfo() {
        UserDao userDao = new UserDao(ds);
        Optional<LoginInfo> maybeLoginInfo = userDao.getLoginInfo("finnsinte");

        assertThat(maybeLoginInfo).isEmpty();
    }

    @Test
    void registerUser() {
        UserDao userDao = new UserDao(ds);
        boolean registered = userDao.register("nytestuser", "Test Person", "testhash");
        assertThat(registered).isTrue();
        assertThat(userDao.getLoginInfo("nytestuser")).isPresent();
        assertThat(userDao.getLoginInfo("nytestuser").get().getPasswordHash()).isEqualTo("testhash");
        assertThat(userDao.getLoginInfo("nytestuser").get().getUserId()).isNotNull();
    }

}