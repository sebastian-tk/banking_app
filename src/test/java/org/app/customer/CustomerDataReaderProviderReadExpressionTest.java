package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.app.customer.CustomerDataReaderProvider.isYesOrNo;
import static org.app.customer.CustomerDataReaderProvider.readExpression;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerDataReaderProviderReadExpressionTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when scanner argument is null")
    public void test1() {
        Scanner scannerTest = null;
        String messageTest = " tekst";

        assertThatThrownBy(() -> readExpression(scannerTest,messageTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scanner cannot be null in readExpression");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when message argument is null")
    public void test2() {
        Scanner scannerTest = new Scanner(System.in);
        String messageTest = null;

        assertThatThrownBy(() -> readExpression(scannerTest,messageTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid message argument in readExpression");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when message argument is empty")
    public void test3() {
        Scanner scannerTest = new Scanner(System.in);
        String messageTest = "";

        assertThatThrownBy(() -> readExpression(scannerTest,messageTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid message argument in readExpression");
    }
}
