package org.app.customer.transaction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.app.customer.transaction.TransactionType.isTypeNotCorrect;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TransactionTypeIsTypeNotCorrectTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when nameEnum argument is null")
    public void test1() {
        String nameEnumTest = null;

        assertThatThrownBy(() -> isTypeNotCorrect(nameEnumTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid nameEnum argument in TransactionType");
    }
    @Test
    @DisplayName("should throws IllegalArgumentException when nameEnum argument is empty")
    public void test2() {
        String nameEnumTest = "";

        assertThatThrownBy(() -> isTypeNotCorrect(nameEnumTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid nameEnum argument in TransactionType");
    }

    @ParameterizedTest
    @DisplayName("should return false when name enum is correct")
    @EnumSource(TransactionType.class)
    public void test3(TransactionType typeTestCorrect) {
        assertThat(isTypeNotCorrect(typeTestCorrect.name())).isFalse();
    }

    @ParameterizedTest
    @DisplayName("should return true when name enum is not correct")
    @ValueSource(strings = {
            "Deposit",
            "DEPOSIt",
            "DEPOSIT.class",
            "WITHDRaW",
            "WITHDRAW.class",
            "WITHDRAWW",
            "DEPOSITT"
    })
    public void test4(String typeTestIncorrect) {
        assertThat(isTypeNotCorrect(typeTestIncorrect)).isTrue();
    }
}
