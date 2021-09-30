package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PeselCreatePeselTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when number argument is null")
    public void test1() {
        String pesel = null;

        assertThatThrownBy(() -> Pesel.createPesel(pesel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number argument");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when number argument is empty")
    public void test2() {
        String pesel = "";

        assertThatThrownBy(() -> Pesel.createPesel(pesel))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number argument");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when length of number is incorrect")
    @ValueSource(strings = {
            "12",
            "111111",
            "1111111111",
            "123456789012"
    })
    public void test4(String pesels) {
        assertThatThrownBy(() -> Pesel.createPesel(pesels))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid length of number");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when number has illegal chars")
    @ValueSource(strings = {
            "12sd43f4589",
            "1234567&899",
            "01232O8923",
            "12@fg$5690"
    })
    public void test5(String pesels) {
        assertThatThrownBy(() -> Pesel.createPesel(pesels))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Illegal characters in the PESEL number");
    }


    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when number has invalid date")
    @ValueSource(strings = {
            "86132673141",
            "81063122731",
            "94083484987",
            "21243123248",
            "93103384610"
    })

    public void test6(String pesels) {
        assertThatThrownBy(() -> Pesel.createPesel(pesels))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number number");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when number has invalid digit of control sum")
    @ValueSource(strings = {
            "86032673144",
            "81061522737",
            "94082184981",
            "89020323244",
            "93100184610"
    })
    public void test7(String pesels) {
        assertThatThrownBy(() -> Pesel.createPesel(pesels))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number number");
    }

    @ParameterizedTest
    @DisplayName("should not throw an exception when the number is correct")
    @ValueSource(strings = {
            "86032673148",
            "81061522731",
            "94082184987",
            "89020323243",
            "96072352198"})
    public void test8(String correctPesels) {
        assertThatCode(() -> Pesel.createPesel(correctPesels)).doesNotThrowAnyException();
    }

}
