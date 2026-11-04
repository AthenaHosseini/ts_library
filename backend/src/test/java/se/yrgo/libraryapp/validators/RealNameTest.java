package se.yrgo.libraryapp.validators;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @Test
    void shouldAcceptNormalName() {
        assertThat(RealName.validate("Athena Hosseini")).isTrue();
    }

    @Test
    void shouldRejectBadWord() {
        assertThat(RealName.validate("Athena banan")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"banan", "ljus", "vatten"})
    void shouldRejectBadWords(String word) {
        assertThat(RealName.validate("Athena " + word)).isFalse();
    }
}