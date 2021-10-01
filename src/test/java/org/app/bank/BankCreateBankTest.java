package org.app.bank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BankCreateBankTest {
    private static final String path = "src/main/java/org/app/data/";

    @Test
    @DisplayName("should throw IllegalArgumentException when customers filename is null")
    public void test1(){
        String fileCustomers = null;
        String fileBusinessCustomers = "businessCustomers.json";
        String filePasswords = "passwords.json";

        assertThatThrownBy(()-> Bank.createBank(fileBusinessCustomers,fileCustomers,filePasswords))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customers file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when customers filename is empty")
    public void test2(){
        String fileCustomers = "";
        String fileBusinessCustomers = "businessCustomers.json";
        String filePasswords = "passwords.json";

        assertThatThrownBy(()-> Bank.createBank(fileBusinessCustomers,fileCustomers,filePasswords))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customers file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when passwords filename is null")
    public void test3(){
        String fileCustomers = "customers.json";
        String fileBusinessCustomers = "businessCustomers.json";
        String filePasswords = null;

        assertThatThrownBy(()-> Bank.createBank(fileBusinessCustomers,fileCustomers,filePasswords))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid passwords file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when customers filename is empty")
    public void test4(){
        String fileCustomers = "customers.json";
        String fileBusinessCustomers = "businessCustomers.json";
        String filePasswords = "";

        assertThatThrownBy(()-> Bank.createBank(fileBusinessCustomers,fileCustomers,filePasswords))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid passwords file name when creating bank");
    }

    @Test
    @DisplayName("should no throw when arguments are correct")
    public void test5(){
        String fileCustomers = path.concat("customers.json");
        String fileBusinessCustomers = path.concat("businessCustomers.json");
        String filePasswords = path.concat("passwords.json");


        assertThatCode(()-> Bank.createBank(fileBusinessCustomers,fileCustomers,filePasswords)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when businessCustomers filename is null")
    public void test6(){
        String fileCustomers = "customers.json";
        String fileBusinessCustomers = null;
        String filePasswords = "password.json";

        assertThatThrownBy(()-> Bank.createBank(fileBusinessCustomers,fileCustomers,filePasswords))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid business customers file name when creating bank");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when businessCustomers filename is empty")
    public void test7(){
        String fileCustomers = "customers.json";
        String fileBusinessCustomers = "";
        String filePasswords = "password.json";

        assertThatThrownBy(()-> Bank.createBank(fileBusinessCustomers,fileCustomers,filePasswords))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid business customers file name when creating bank");
    }


}
