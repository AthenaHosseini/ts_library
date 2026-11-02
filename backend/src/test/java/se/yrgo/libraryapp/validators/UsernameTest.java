package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UsernameTest {
    @Test
    void correctUsername() {
        assertThat(Username.validate("bosse")).isTrue();
    }

    @Test
    void incorrectUsername() {
        assertThat(Username.validate("name with space")).isFalse();
    }

    @Test
    void usernameWithNumbersShouldBeValid() {
        assertThat(Username.validate("Hampus123")).isTrue();
    }

    @Test
    void usernameTooShort() {
        assertThat(Username.validate("hej")).isFalse();
    }

    @Test
    void usernameWithFourLetters() {
        assertThat(Username.validate("Saga")).isTrue();
    }

    @Test
    void emptyUsername() {
        assertThat(Username.validate("")).isFalse();
    }

    @Test
    void nullUsernameShouldBeInvalid() {
        assertThat(Username.validate(null)).isFalse();
    }
}
