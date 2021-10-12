package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.app.customer.ValidatorPersonalData.isEmailNotCorrect;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidatorPersonalDataIsEmailNotCorrectTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when email argument is null")
    public void test1() {
        String emailTest = null;

        assertThatThrownBy(() -> isEmailNotCorrect(emailTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email argument when check syntax email");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when email argument is empty")
    public void test2() {
        String emailTest = "";

        assertThatThrownBy(() -> isEmailNotCorrect(emailTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email argument when check syntax email");
    }

    @ParameterizedTest
    @DisplayName("should return true if syntax email is not correct")
    @CsvFileSource(resources = "/incorrectEmails.csv")
    public void test3(String incorrectEmail) {

        assertThat(isEmailNotCorrect(incorrectEmail)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("should return true if syntax email is correct")
    @CsvFileSource(resources = "/correctEmails.csv")
    public void test4(String correctEmail) {

        assertThat(isEmailNotCorrect(correctEmail)).isFalse();
    }
}
