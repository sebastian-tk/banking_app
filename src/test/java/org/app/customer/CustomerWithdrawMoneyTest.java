package org.app.customer;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

public class CustomerWithdrawMoneyTest {
    private static Customer customer;
    private static Account account;

    @Test
    @BeforeAll
    public static void initialCustomer() {
        String name = "Name";
        String surname = "Surname";
        String pesel = "78092475727";
        String address = "ul. Adama Mickiewicza 10/23 10-100 Warszawa";
        String email = "firstword.secondword@gmail.com";
        String phoneNumber = "123-456-789";
        account = Account.createAccount("ING", new BigInteger("12345678901234567890123456"), new BigDecimal("100"));
        Set<Account> accountSet = Set.of(account);

        customer = Customer.createCustomer(name, surname, pesel, address, email, phoneNumber, accountSet);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when money argument is null")
    public void test1() {
        BigDecimal testMoney = null;

        Assertions.assertThatThrownBy(() -> customer.withdrawMoney(testMoney, account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument when withdrawing");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when money argument is equal zero")
    public void test2() {
        BigDecimal testMoney = new BigDecimal("0.0");

        Assertions.assertThatThrownBy(() -> customer.withdrawMoney(testMoney, account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument when withdrawing");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when money argument is less than zero")
    public void test3() {
        BigDecimal testMoney = new BigDecimal("-1.0");

        Assertions.assertThatThrownBy(() -> customer.withdrawMoney(testMoney, account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument when withdrawing");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when account argument is null")
    public void test4() {
        BigDecimal testMoney = new BigDecimal("1.0");
        Account accountTest = null;

        Assertions.assertThatThrownBy(() -> customer.withdrawMoney(testMoney, accountTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid account argument when withdrawing");
    }
}

