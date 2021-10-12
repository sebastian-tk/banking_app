package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.app.customer.ValidatorPersonalData.isNumberNotCorrect;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidatorPersonalDataIsNumberNotCorrectTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when phone number argument is null")
    public void test1() {
        String phoneNumberTest = null;

        assertThatThrownBy(() -> isNumberNotCorrect(phoneNumberTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number argument when check syntax phone number");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when phone number argument is empty")
    public void test2() {
        String phoneNumberTest = "";

        assertThatThrownBy(() -> isNumberNotCorrect(phoneNumberTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number argument when check syntax phone number");
    }

    @ParameterizedTest
    @DisplayName("should return true if phone number are not correct")
    @CsvFileSource(resources = "/incorrectPhoneNumbers.csv")
    public void test3(String incorrectAdres) {

        assertThat(isNumberNotCorrect(incorrectAdres)).isTrue();
    }

    @Test
    @DisplayName("should return false if number are correct")
    public void test4() {
        String numberCorrect = "123-456-789";

        assertThat(isNumberNotCorrect(numberCorrect)).isFalse();
    }
}
