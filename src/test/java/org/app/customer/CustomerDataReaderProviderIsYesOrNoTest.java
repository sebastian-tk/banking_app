package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.app.customer.CustomerDataReaderProvider.isYesOrNo;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerDataReaderProviderIsYesOrNoTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when scanner argument is null")
    public void test1() {
        Scanner scannerTest = null;
        String questionTest = " tekst";

        assertThatThrownBy(() -> isYesOrNo(scannerTest,questionTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scanner cannot be null in isYesOrNo");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when question argument is null")
    public void test2() {
        Scanner scannerTest = new Scanner(System.in);
        String questionTest = null;

        assertThatThrownBy(() -> isYesOrNo(scannerTest,questionTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid question argument in isYesOrNo");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when question argument is empty")
    public void test3() {
        Scanner scannerTest = new Scanner(System.in);
        String questionTest = "";

        assertThatThrownBy(() -> isYesOrNo(scannerTest,questionTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid question argument in isYesOrNo");
    }
}
