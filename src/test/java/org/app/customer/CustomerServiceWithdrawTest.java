package org.app.customer;

import org.app.customer.extensions.ParametersResolverForAccount;
import org.app.customer.extensions.ParametersResolverForCustomer;
import org.app.customer.transaction.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ParametersResolverForCustomer.class)
@ExtendWith(ParametersResolverForAccount.class)
public record CustomerServiceWithdrawTest(Customer customer, Account serveAccountCorrect) {
    private static Scanner scannerCorrect;
    private static List<Transaction> transactionListCorrect;

    @BeforeAll
    public static void init() {
        scannerCorrect = new Scanner(System.in);
        transactionListCorrect = new ArrayList<>();
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when scanner argument is null")
    public void test1() {
        Scanner scannerTest = null;
        assertThatThrownBy(() -> customer.serviceWithdraw(scannerTest, serveAccountCorrect, transactionListCorrect))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid scanner argument in serviceWithdraw");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when serveAccount argument is null")
    public void test2() {
        Account serveAccountTest=null;
        assertThatThrownBy(() -> customer.serviceWithdraw(scannerCorrect, serveAccountTest, transactionListCorrect))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid serveAccount argument in serviceWithdraw");
    }
    @Test
    @DisplayName("should throws IllegalArgumentExceptions when transactionList argument is null")
    public void test3() {
        List<Transaction> transactionListTest = null;
        assertThatThrownBy(() -> customer.serviceWithdraw(scannerCorrect, serveAccountCorrect, transactionListTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid transactionAccount argument in serviceWithdraw");
    }

}
