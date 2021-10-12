package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.app.customer.EncryptedPassword.mergeHash;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EncryptedPasswordMergeHashTest {
    private static final String correctHashPassword = "EB9A8996FB077B1EFCE23CED31B0804C";
    private static final byte[] correctSalt ={52, 6, -11, 6, 38, -126, 77, 38, 73, -37, 95, -59, -55, -1, -103, 3};

    @Test
    @DisplayName("should throws IllegalArgumentException when salt argument is null")
    public void test1() {
        byte[] saltTest = null;

        assertThatThrownBy(() -> mergeHash(saltTest,correctHashPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid salt argument when try merge hash");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when length password is equal zero")
    public void test2() {
        byte[] saltTest = new byte[0];

        assertThatThrownBy(() -> mergeHash(saltTest,correctHashPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid salt argument when try merge hash");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when encrypted password argument is null")
    public void test3() {
        String passwordTest = null;

        assertThatThrownBy(() -> mergeHash(correctSalt,passwordTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid encryptedPassword argument when try merge hash");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when encrypted password is empty")
    public void test4() {
        String passwordTest = "";

        assertThatThrownBy(() -> mergeHash(correctSalt,passwordTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid encryptedPassword argument when try merge hash");
    }

    @Test
    @DisplayName("should return a valid result based on the input arguments")
    public void test5() {
        String expectedMergedHash = "3406F50626824D2649DB5FC5C9FF9903:EB9A8996FB077B1EFCE23CED31B0804C";

        assertThat(mergeHash(correctSalt,correctHashPassword)).isEqualTo(expectedMergedHash);
    }
}
