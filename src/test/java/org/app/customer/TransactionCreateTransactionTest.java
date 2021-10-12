package org.app.customer;

import org.app.customer.extensions.ParametersResolverForTransaction;
import org.app.customer.transaction.Transaction;
import org.app.customer.transaction.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigDecimal;

import static org.app.customer.transaction.Transaction.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ParametersResolverForTransaction.class)
public record TransactionCreateTransactionTest(Transaction correctTransaction) {
    @Test
    @DisplayName("should throws IllegalArgumentException when pesel argument is null")
    public void test1() {
        Pesel peselTest = null;

        assertThatThrownBy(() -> createTransaction(peselTest,
                                                    correctTransaction.getType(),
                                                    correctTransaction.getMoney(),
                                                    correctTransaction.getDate(),
                                                    correctTransaction.getAccount()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid pesel argument when create Transaction");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when transaction type argument is null")
    public void test2() {
        TransactionType typeTest = null;

        assertThatThrownBy(() -> createTransaction(correctTransaction.getPesel(),
                typeTest,
                correctTransaction.getMoney(),
                correctTransaction.getDate(),
                correctTransaction.getAccount()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid type transaction argument when create Transaction");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when money argument is null")
    public void test3() {
        BigDecimal moneyTest = null;

        assertThatThrownBy(() -> createTransaction(correctTransaction.getPesel(),
                correctTransaction.getType(),
                moneyTest,
                correctTransaction.getDate(),
                correctTransaction.getAccount()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument when create Transaction");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when date argument is null")
    public void test4() {
        String dateTest = null;

        assertThatThrownBy(() -> createTransaction(correctTransaction.getPesel(),
                correctTransaction.getType(),
                correctTransaction.getMoney(),
                dateTest,
                correctTransaction.getAccount()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid date argument when create Transaction");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when account argument is null")
    public void test5() {
        Account accountTest = null;

        assertThatThrownBy(() -> createTransaction(correctTransaction.getPesel(),
                correctTransaction.getType(),
                correctTransaction.getMoney(),
                correctTransaction.getDate(),
                accountTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid account argument when create Transaction");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when money is not positive")
    @CsvFileSource(resources = "/notPositiveValuesMoney.csv")
    public void test6(String incorrectMoney) {
        assertThatThrownBy(() -> createTransaction(correctTransaction.getPesel(),
                correctTransaction.getType(),
                new BigDecimal(incorrectMoney),
                correctTransaction.getDate(),
                correctTransaction.getAccount()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid money argument.Should be positive value");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when date syntax is not correct")
    @CsvFileSource(resources = "/incorrectDates.csv")
    public void test7(String incorrectDates) {
        assertThatThrownBy(() -> createTransaction(correctTransaction.getPesel(),
                correctTransaction.getType(),
                correctTransaction.getMoney(),
                incorrectDates,
                correctTransaction.getAccount()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid date argument.Incorrect syntax");
    }

}
