package org.app.bank;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BankCreateBankTest {
    private static String path;
    private static String correctFileNameCustomers;
    private static String correctFileNameBusinessCustomers;
    private static String correctFileNamePasswords;
    private static String correctFileNameTransactions;

    @BeforeAll
    public static void initName(){
        path = "src/main/java/org/app/data/";
        correctFileNameCustomers = "customers.json";
        correctFileNameBusinessCustomers = "businessCustomers.json";
        correctFileNamePasswords = "passwords.json";
        correctFileNameTransactions = "transactionsHistory.json";

    }
    @Test
    @DisplayName("should throw IllegalArgumentException when customers filename is null")
    public void test1(){
        String fileNameCustomersTest = null;

        assertThatThrownBy(()-> Bank.createBank(correctFileNameBusinessCustomers,fileNameCustomersTest,correctFileNamePasswords,correctFileNameTransactions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customers file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when customers filename is empty")
    public void test2(){
        String fileNameCustomersTest = null;

        assertThatThrownBy(()-> Bank.createBank(correctFileNameBusinessCustomers,fileNameCustomersTest,correctFileNamePasswords,correctFileNameTransactions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customers file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when passwords filename is null")
    public void test3(){
        String fileNamePasswordsTest = null;

        assertThatThrownBy(()-> Bank.createBank(correctFileNameBusinessCustomers,correctFileNameCustomers,fileNamePasswordsTest,correctFileNameTransactions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid passwords file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when customers filename is empty")
    public void test4(){
        String fileNamePasswordsTest = "";

        assertThatThrownBy(()-> Bank.createBank(correctFileNameBusinessCustomers,correctFileNameCustomers,fileNamePasswordsTest,correctFileNameTransactions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid passwords file name when creating bank");
    }

    @Test
    @DisplayName("should no throw when arguments are correct")
    public void test5(){
        String fullPathFileCustomers = path.concat(correctFileNameCustomers);
        String fullPathFileBusinessCustomers = path.concat(correctFileNameBusinessCustomers);
        String fullPathFilePasswords = path.concat(correctFileNamePasswords);
        String fullPathFileTransactions = path.concat(correctFileNameTransactions);

        assertThatCode(()-> Bank
                .createBank(fullPathFileBusinessCustomers,fullPathFileCustomers,fullPathFilePasswords,fullPathFileTransactions))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when businessCustomers filename is null")
    public void test6(){
        String fileNameBusinessCustomersTest = null;

        assertThatThrownBy(()-> Bank.createBank(fileNameBusinessCustomersTest,correctFileNameCustomers,correctFileNamePasswords,correctFileNameTransactions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid business customers file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when businessCustomers filename is empty")
    public void test7(){
        String fileNameBusinessCustomersTest = "";

        assertThatThrownBy(()-> Bank.createBank(fileNameBusinessCustomersTest,correctFileNameCustomers,correctFileNamePasswords,correctFileNameTransactions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid business customers file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when transactions filename is null")
    public void test8(){
        String fileNameTransactionsTest = null;

        assertThatThrownBy(()-> Bank.createBank(correctFileNameBusinessCustomers,correctFileNameCustomers,correctFileNamePasswords,fileNameTransactionsTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid transactions file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when transactions filename is empty")
    public void test9(){
        String fileNameTransactionsTest = "";

        assertThatThrownBy(()-> Bank.createBank(correctFileNameBusinessCustomers,correctFileNameCustomers,correctFileNamePasswords,fileNameTransactionsTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid transactions file name when creating bank");
    }
}
