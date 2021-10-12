package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerDataReaderProviderReadChoiceTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when scanner argument is null")
    public void test1() {
        Scanner scannerTest = null;
        int amountChoiceTest = 1;

        assertThatThrownBy(() -> CustomerDataReaderProvider.readChoice(scannerTest,amountChoiceTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scanner cannot be null in readChoice");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when amountOptions is negative")
    public void test2() {
        Scanner scannerTest = new Scanner(System.in);
        int amountChoiceTest = -1;

        assertThatThrownBy(() -> CustomerDataReaderProvider.readChoice(scannerTest,amountChoiceTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid amountOptions argument.Must be positive");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when amountOptions is zero")
    public void test3() {
        Scanner scannerTest = new Scanner(System.in);
        int amountChoiceTest = 0;

        assertThatThrownBy(() -> CustomerDataReaderProvider.readChoice(scannerTest,amountChoiceTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid amountOptions argument.Must be positive");
    }

}
