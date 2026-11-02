package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.EmptySource;

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

    @Test
    void usernameWithInvalidLetters() {
        assertThat(Username.validate("Saga!")).isFalse();
    }

    @Test
    void usernameWithAllowedSpecialLetters() {
        assertThat(Username.validate("saga_123")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"saga", "saga123", "saga_123", "test.user"})
    void validUsernames(String username) {
        assertThat(Username.validate(username)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "saga!", "saga med mellanslag", "saga#"})
    void invalidUsernames(String username) {
        assertThat(Username.validate(username)).isFalse();
    }

    @ParameterizedTest
    @EmptySource
    void emptyUsernameShouldBeInvalid(String username) {
        assertThat(Username.validate(username)).isFalse();
    }

}
