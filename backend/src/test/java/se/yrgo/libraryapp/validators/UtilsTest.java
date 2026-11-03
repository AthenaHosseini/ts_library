package se.yrgo.libraryapp.validators;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;


import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

    @Test
    void shouldRemoveNumbers() {
        assertThat(Utils.onlyLettersAndWhitespace("saga123")).isEqualTo("saga");
    }

    @Test 
    void shouldKeepSpaces() {
        assertThat(Utils.onlyLettersAndWhitespace("Saga 123 Test")).isEqualTo("saga  test");
    }

    @Test
    void shouldConvertLeetNumbers() {
        assertThat(Utils.cleanAndUnLeet("s4g4")).isEqualTo("saga");
    }

    @Test
    void shouldRemoveSymbols() {
        assertThat(Utils.onlyLettersAndWhitespace("Saga!#")).isEqualTo("saga");
    }

    @ParameterizedTest
    @ValueSource(strings = {"s4g4", "S4G4", "saga"})
    void shouldCleanNames(String name) {
        assertThat(Utils.cleanAndUnLeet(name)).isEqualTo("saga");
    }

    @ParameterizedTest
    @EmptySource
    void shouldHandleEmptyString(String text) {
        assertThat(Utils.onlyLettersAndWhitespace(text)).isEqualTo("");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Saga!", "Saga123", "S#a#g#a"})
    void shouldRemoveNumbersAndSymbols(String text) {
        assertThat(Utils.onlyLettersAndWhitespace(text)).isEqualTo("saga");
    }
}