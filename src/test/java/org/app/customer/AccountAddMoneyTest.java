package org.app.customer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.*;

public class AccountAddMoneyTest {
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
    @DisplayName("should throw IllegalArgumentException when moneyToAdd is null")
    public void test1(){
        BigDecimal moneyToAdd = null;

        assertThatThrownBy(()-> accountTest.addMoney(moneyToAdd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect addMoney argument");
    }
    @Test
    @DisplayName("should throw IllegalArgumentException when moneyToAdd is equal zero")
    public void test2(){
        BigDecimal moneyToAdd = BigDecimal.ZERO;

        assertThatThrownBy(()-> accountTest.addMoney(moneyToAdd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect addMoney argument");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when moneyToAdd is less than zero")
    public void test3(){
        BigDecimal moneyToAdd = new BigDecimal("-1.0");

        assertThatThrownBy(()-> accountTest.addMoney(moneyToAdd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect addMoney argument");
    }
    @Test
    @DisplayName("should not throw Exception when moneyToAdd is positive")
    public void test4(){
        BigDecimal moneyToAdd = new BigDecimal("1.0");

        assertThatCode(()-> accountTest.addMoney(moneyToAdd)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should  increase the amount of account by the added value")
    public void test5(){
        BigDecimal moneyToAdd = new BigDecimal("100.0");
        BigDecimal expectedAmountMoney = accountTest.getAmountMoney().add(moneyToAdd);

        accountTest.addMoney(moneyToAdd);

        assertThat(accountTest.getAmountMoney()).isEqualTo(expectedAmountMoney);
    }

}
