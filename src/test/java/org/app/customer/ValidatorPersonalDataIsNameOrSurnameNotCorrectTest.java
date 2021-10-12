package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.app.customer.ValidatorPersonalData.isNameOrSurnameNotCorrect;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidatorPersonalDataIsNameOrSurnameNotCorrectTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when expression argument is null")
    public void test1() {
        String expressionTest = null;

        assertThatThrownBy(() -> isNameOrSurnameNotCorrect(expressionTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid expression argument when check syntax name/surname");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when expression argument is empty")
    public void test2() {
        String expressionTest = "";

        assertThatThrownBy(() -> isNameOrSurnameNotCorrect(expressionTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid expression argument when check syntax name/surname");
    }

    @ParameterizedTest
    @DisplayName("should return true if expression are not correct")
    @CsvFileSource(resources = "/incorrectNameSurname.csv")
    public void test3(String incorrectName) {

        assertThat(isNameOrSurnameNotCorrect(incorrectName)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("should return true if expression is correct")
    @CsvFileSource(resources = "/correctNameSurname.csv")
    public void test4(String correctName) {

        assertThat(isNameOrSurnameNotCorrect(correctName)).isFalse();
    }

}
