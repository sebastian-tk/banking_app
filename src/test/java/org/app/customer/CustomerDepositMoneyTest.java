package org.app.customer;


import org.app.customer.extensions.ParametersResolverForAccount;
import org.app.customer.extensions.ParametersResolverForCustomer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;

@ExtendWith(ParametersResolverForCustomer.class)
@ExtendWith(ParametersResolverForAccount.class)
public record CustomerDepositMoneyTest(Customer customer, Account account) {

    @Test
    @DisplayName("should throw IllegalArgumentException when money argument is null")
    public void test1() {
        BigDecimal testMoney = null;
        Assertions.assertThatThrownBy(() -> customer.depositMoney(testMoney, account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument when depositing");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when money argument is equal zero")
    public void test2() {
        BigDecimal testMoney = new BigDecimal("0.0");

        Assertions.assertThatThrownBy(() -> customer.depositMoney(testMoney, account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument when depositing");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when money argument is less than zero")
    public void test3() {
        BigDecimal testMoney = new BigDecimal("-1.0");

        Assertions.assertThatThrownBy(() -> customer.depositMoney(testMoney, account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument when depositing");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when account argument is null")
    public void test4() {
        BigDecimal testMoney = new BigDecimal("1.0");
        Account accountTest = null;

        Assertions.assertThatThrownBy(() -> customer.depositMoney(testMoney, accountTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid account argument when depositing");
    }

}

