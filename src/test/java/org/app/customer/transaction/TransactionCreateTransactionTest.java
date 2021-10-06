package org.app.customer.transaction;

import org.app.customer.Account;
import org.app.customer.Pesel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCreateTransactionTest {
    private static Pesel correctPesel;
    private static TransactionType correctType;
    private static BigDecimal correctMoney;
    private static String correctDate;
    private static Account correctAccount;

    @BeforeAll
    public static void init(){
        correctPesel = Pesel.createPesel("58021743822");
        correctType = TransactionType.WITHDRAW;
        correctMoney = new BigDecimal("1.0");
        correctDate = "2021-08-01";
        correctAccount = Account.createAccount("personal", new BigInteger("12345678901234567890123456"),new BigDecimal("0"));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when pesel argument is null")
    public void test1(){
        Pesel peselTest = null;

        Assertions.assertThatThrownBy(()-> Transaction.createTransaction(peselTest,correctType,correctMoney,correctDate,correctAccount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid pesel argument when create Transaction");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when transaction type argument is null")
    public void test2(){
        TransactionType typeTransactionTest = null;

        Assertions.assertThatThrownBy(()-> Transaction.createTransaction(correctPesel,typeTransactionTest,correctMoney,correctDate,correctAccount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid type transaction argument when create Transaction");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when money argument is null")
    public void test3(){
        BigDecimal moneyTest = null;

        Assertions.assertThatThrownBy(()-> Transaction.createTransaction(correctPesel,correctType,moneyTest,correctDate,correctAccount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument when create Transaction");
    }

    @ParameterizedTest
    @DisplayName("should throw IllegalArgumentException when money argument is not positive")
    @ValueSource(strings = {
                            "-0.0000001",
                            "-1.0",
                            "-10000000000000.0"})
    public void test4(String moneyTest) {
        Assertions.assertThatThrownBy(() -> Transaction.createTransaction(correctPesel, correctType, new BigDecimal(moneyTest), correctDate, correctAccount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument.Should be positive value");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when date argument is null")
    public void test5(){
        String dateTest = null;

        Assertions.assertThatThrownBy(()-> Transaction.createTransaction(correctPesel,correctType,correctMoney,dateTest,correctAccount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid date argument when create Transaction");
    }

    @ParameterizedTest
    @DisplayName("should throw IllegalArgumentException when syntax date is not correct")
    @ValueSource(strings = {
            "20-02-2001",
            "31-02-2000",
            "date",
            "03-31-1990",
            "2001-31-02",
            "2001-02-31",
            "2001-02-29",})
    public void test6(String dateTest) {
        Assertions.assertThatThrownBy(() -> Transaction.createTransaction(correctPesel, correctType, correctMoney, dateTest, correctAccount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid date argument.Incorrect syntax");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when account argument is null")
    public void test7(){
        Account accountTest = null;

        Assertions.assertThatThrownBy(()-> Transaction.createTransaction(correctPesel,correctType,correctMoney,correctDate,accountTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid account argument when create Transaction");
    }
}