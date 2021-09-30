package org.app.customer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.*;

public class AccountSubtractMoneyTest {
    private static Account accountTest;

    @Test
    @BeforeAll
    @DisplayName("method initializing object Account")
    public static  void init(){
        String name = "example name";
        BigInteger number = new BigInteger("12345678901234567890123456");
        BigDecimal amountMoney = new BigDecimal("100.00");

        accountTest = Account.createAccount(name,number,amountMoney);
    }

    @Test
    @DisplayName("should throw Illegal argument exception when moneyToGet is null")
    public void test1(){
        BigDecimal amountToGet = null;

        assertThatThrownBy(()-> accountTest.subtractMoney(amountToGet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid moneyToGet argument");
    }

    @Test
    @DisplayName("should throw Illegal argument exception when moneyToGet is equal zero")
    public void test2(){
        BigDecimal amountToGet = BigDecimal.ZERO;

        assertThatThrownBy(()-> accountTest.subtractMoney(amountToGet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid moneyToGet argument");
    }


    @Test
    @DisplayName("should throw Illegal argument exception when moneyToGet is less than zero")
    public void test3(){
        BigDecimal amountToGet = new BigDecimal("-1.0");

        assertThatThrownBy(()-> accountTest.subtractMoney(amountToGet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid moneyToGet argument");
    }


    @Test
    @DisplayName("should not throw Exception when moneyToGet is positive")
    public void test4(){
        BigDecimal moneyToGet = new BigDecimal("1.0");

        assertThatCode(()-> accountTest.subtractMoney(moneyToGet)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should  increase the amount of account by the added value")
    public void test5(){
        BigDecimal moneyToGet = new BigDecimal("100.0");
        BigDecimal expectedAmountMoney = accountTest.getAmountMoney().subtract(moneyToGet);

        accountTest.subtractMoney(moneyToGet);

        assertThat(accountTest.getAmountMoney()).isEqualTo(expectedAmountMoney);
    }
}
