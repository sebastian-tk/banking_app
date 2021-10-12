package org.app.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.app.customer.EncryptedPassword.*;
import static org.assertj.core.api.Assertions.assertThat;
public class EncryptedPasswordGenerateSaltTest {
    @Test
    @DisplayName("should return salt with length equal BYTES_SALT_LENGTH")
    public void test1() {
        assertThat(generateSalt()).hasSize(BYTES_SALT_LENGTH);
    }
}
