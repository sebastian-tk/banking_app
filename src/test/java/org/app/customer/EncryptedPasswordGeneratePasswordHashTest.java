package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.app.customer.EncryptedPassword.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EncryptedPasswordGeneratePasswordHashTest {

    @Test
    @DisplayName("should throws IllegalArgumentException when password argument is null")
    public void test1() {
        char[] passwordTest = null;
        byte[] saltTest = new byte[1];

        assertThatThrownBy(() -> generatePasswordHash(passwordTest,saltTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when generate password hash");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when length password is equal zero")
    public void test2() {
        char[] passwordTest = new char[0];
        byte[] saltTest = new byte[1];

        assertThatThrownBy(() -> generatePasswordHash(passwordTest,saltTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when generate password hash");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when salt argument is null")
    public void test3() {
        char[] passwordTest = new char[1];
        byte[] saltTest = null;

        assertThatThrownBy(() -> generatePasswordHash(passwordTest,saltTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid salt argument when generate password hash");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when length salt is equal zero")
    public void test4() {
        char[] passwordTest = new char[1];
        byte[] saltTest = new byte[0];

        assertThatThrownBy(() -> generatePasswordHash(passwordTest,saltTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid salt argument when generate password hash");
    }
    @Test
    @DisplayName("should return correct hashPassword")
    public void test5() {
        char[] passwordTest = "adam1234".toCharArray();
        byte[] saltTest = {52, 6, -11, 6, 38, -126, 77, 38, 73, -37, 95, -59, -55, -1, -103, 3};
        String hashExpected = "EB9A8996FB077B1EFCE23CED31B0804C";

        String generatedHash = generatePasswordHash(passwordTest,saltTest);

        assertThat(generatedHash).isEqualTo(hashExpected);
    }
}
