package org.app.customer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerServiceTest {
    private static Customer customerTest;

    @BeforeAll
    public static void init(){
        customerTest = Customer.createCustomer(
                "Name",
                "Surname",
                "78092475727",
                "ul. Adama Mickiewicza 10/23 10-100 Warszawa",
                "firstword.secondword@gmail.com",
                "111-111-111",
                Set.of(Account.createAccount("ING", new BigInteger("12345678901234567890123456"), new BigDecimal("0"))));
    }


    @Test
    @DisplayName("should throws IllegalArgumentExceptions when hashPassword argument is null")
    public void test1() {
        String hashPassword = null;
        byte[] salt = {10,20,30,-20,40,50,12,45,10,20,30,-20,40,50,12,45};
        assertThatThrownBy(() -> customerTest.service(hashPassword,salt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid hashPassword argument in service");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when hashPassword argument is empty")
    public void test2() {
        String hashPassword = "";
        byte[] salt = {10,20,30,-20,40,50,12,45,10,20,30,-20,40,50,12,45};
        assertThatThrownBy(() -> customerTest.service(hashPassword,salt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid hashPassword argument in service");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when salt argument is null")
    public void test3() {
        String hashPassword = "hash";
        byte[] salt = null;
        assertThatThrownBy(() -> customerTest.service(hashPassword,salt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid salt argument in service");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when size salt is zero")
    public void test4() {
        String hashPassword = "hash";
        byte[] salt = new byte[0];
        assertThatThrownBy(() -> customerTest.service(hashPassword,salt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid salt argument in service");
    }

}
