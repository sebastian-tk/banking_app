package org.app.customer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class AccountCreateAccountTest {
    private static String correctName;
    private static BigInteger correctNumber;
    private static BigDecimal correctAmountMoney;

    @BeforeAll
    public static void init(){
        correctName = "Name";
        correctNumber = new BigInteger("12345678901234567890123456");
        correctAmountMoney = new BigDecimal("1");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when name argument is null")
    public void test1() {
        String nameTest = null;

        Throwable throwableAccount = catchThrowable(()-> Account.createAccount(nameTest, correctNumber, correctAmountMoney));

        assertThat(throwableAccount)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name argument when creating an account");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when name argument is empty")
    public void test2(){
        String nameTest = "";

        Throwable throwableAccount = catchThrowable(()-> Account.createAccount(nameTest,correctNumber,correctAmountMoney));

        assertThat(throwableAccount)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name argument when creating an account");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when number argument is null")
    public void test3(){
        BigInteger numberTest = null;

        Throwable throwableAccount = catchThrowable(()-> Account.createAccount(correctName,numberTest,correctAmountMoney));

        assertThat(throwableAccount)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number argument when creating an account");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when number of digits is different from 26")
    @MethodSource("bigIntegerProvider")
    public void test4(BigInteger numbersTest){

        Throwable throwableAccount = catchThrowable(()-> Account.createAccount(correctName,numbersTest,correctAmountMoney));

        assertThat(throwableAccount)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid number argument when creating an account");
    }

    static Stream<BigInteger> bigIntegerProvider(){
        return Stream.of(
                new BigInteger("0"),
                new BigInteger("123456789012345"),
                new BigInteger("123456789012345678901234567890"));
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when amountMoney is null")
    public void test5(){
        BigDecimal amountMoneyTest = null;

        Throwable throwableAccount = catchThrowable(()-> Account.createAccount(correctName,correctNumber,amountMoneyTest));

        assertThat(throwableAccount)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid amountMoney argument when creating an account");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when amountMoney is less then zero")
    public void test6(){
        BigDecimal amountMoneyTest = new BigDecimal("-1.0");

        Throwable throwableAccount = catchThrowable(()->Account.createAccount(correctName,correctNumber,amountMoneyTest));

        assertThat(throwableAccount)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid amountMoney argument when creating an account");
    }
    @Test
    @DisplayName("should not throws  when amountMoney is equals with zero")
    public void test7(){
        BigDecimal amountMoneyTest = new BigDecimal("0");

        Throwable throwableAccount = catchThrowable(()->Account.createAccount(correctName,correctNumber,amountMoneyTest));

        assertThat(throwableAccount).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("should not throws  when amountMoney is greater than zero")
    @MethodSource("bigDecimalProvider")
    public void test8(BigDecimal amountsPositiveMoney){

        Throwable throwableAccount = catchThrowable(()-> Account.createAccount(correctName,correctNumber,amountsPositiveMoney));

        assertThat(throwableAccount).doesNotThrowAnyException();
    }

    public static Stream<BigDecimal> bigDecimalProvider(){
        return Stream.of(
                new BigDecimal("0.001"),
                new BigDecimal("123.45"),
                new BigDecimal("1000000.00")
        );
    }

    @Test
    @DisplayName("should return object account the same as passed  by argument")
    public void  test9(){

        Account accountTest = Account.createAccount(correctName,correctNumber,correctAmountMoney);

        assertThat(accountTest.getName()).isEqualTo(correctName);
        assertThat(accountTest.getNumber()).isEqualTo(correctNumber);
        assertThat(accountTest.getAmountMoney()).isEqualTo(correctAmountMoney);
    }
}
