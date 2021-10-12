package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.app.customer.ValidatorPersonalData.isAddressNotCorrect;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidatorPersonalDataIsAddressNotCorrectTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when address argument is null")
    public void test1() {
        String addressTest = null;

        assertThatThrownBy(() -> isAddressNotCorrect(addressTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid address argument when check syntax address");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when address argument is empty")
    public void test2() {
        String addressTest = "";

        assertThatThrownBy(() -> isAddressNotCorrect(addressTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid address argument when check syntax address");
    }

    @ParameterizedTest
    @DisplayName("should return true if addresses are not correct")
    @CsvFileSource(resources = "/incorrectAddresses.csv")
    public void test3(String incorrectAdres) {

        assertThat(isAddressNotCorrect(incorrectAdres)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("should return false if addresses are correct")
    @CsvFileSource(resources = "/correctAddresses.csv")
    public void test4(String incorrectAdres) {

        assertThat(isAddressNotCorrect(incorrectAdres)).isFalse();
    }
}
