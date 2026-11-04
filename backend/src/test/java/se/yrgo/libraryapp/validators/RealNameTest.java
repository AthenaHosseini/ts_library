package se.yrgo.libraryapp.validators;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @Test
    void shouldAcceptNormalName() {
        assertThat(RealName.validate("Anna Svensson")).isTrue();
    }

    @Test
    void shouldRejectBadWord() {
        assertThat(RealName.validate("Anna banan")).isFalse();
    }
}