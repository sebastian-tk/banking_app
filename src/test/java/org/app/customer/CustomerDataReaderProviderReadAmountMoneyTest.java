package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.app.customer.CustomerDataReaderProvider.isYesOrNo;
import static org.app.customer.CustomerDataReaderProvider.readAmountMoney;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerDataReaderProviderReadAmountMoneyTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when scanner argument is null")
    public void test1() {
        Scanner scannerTest = null;

        assertThatThrownBy(() -> readAmountMoney(scannerTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scanner cannot be null in readAmountMoney");
    }
}
