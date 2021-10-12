package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.app.customer.EncryptedPassword.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EncryptedPasswordGenerateFullHashTest {

    @Test
    @DisplayName("should throws IllegalArgumentException when password argument is null")
    public void test1() {
        char[] passwordTest = null;

        assertThatThrownBy(() -> generateFullHash(passwordTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when generate full password hash");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when length password is equal zero")
    public void test2() {
        char[] passwordTest = new char[0];

        assertThatThrownBy(() -> generateFullHash(passwordTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when generate full password hash");
    }
}
