package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.app.customer.EncryptedPassword.clearPassword;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EncryptedPasswordClearPasswordTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when password argument is null")
    public void test1() {
        char[] passwordTest = null;

        assertThatThrownBy(() -> clearPassword(passwordTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when clear password");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when length password is equal zero")
    public void test2() {
        char[] passwordTest = new char[0];

        assertThatThrownBy(() -> clearPassword(passwordTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when clear password");
    }
}
