package org.app.customer;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.app.customer.EncryptedPassword.createEncryptedPassword;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class EncryptedPasswordCreateEncryptedPasswordTest {
    private static final String correctPassword= "AA2D4E52F59482B34EB0CFCA116A2F42:FF0ADBFD53226F81DAF040FB8F55FA3B";
    @InjectMocks
    private Pesel correctPesel;

    @Test
    @DisplayName("should throws IllegalArgumentException when pesel argument is null")
    public void test1() {
        Pesel peselTest = null;

        assertThatThrownBy(() -> createEncryptedPassword(peselTest,correctPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid pesel argument when create encrypt password");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when password argument is null")
    public void test2(){
        String passwordTest = null;

        assertThatThrownBy(() -> createEncryptedPassword(correctPesel,passwordTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when create encrypt password");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when password argument is incorrect")
    @CsvFileSource(resources = "/incorrectPasswordsHash.csv")
    public void test3(String incorrectPassword){

        assertThatThrownBy(() -> createEncryptedPassword(correctPesel,incorrectPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when create encrypt password");
    }
    @Test
    @DisplayName("should method return the object with the same password as input argument")
    public void test4(){
        String expectedPassword = correctPassword.split(":")[1];

        EncryptedPassword encryptedPasswordTest = createEncryptedPassword(correctPesel,correctPassword);

        assertAll(
                "Test new EncryptedPassword",
                ()-> assertThat(encryptedPasswordTest.getPesel()).isEqualTo(correctPesel),
                ()-> assertThat(encryptedPasswordTest.getHashPassword()).isEqualTo(expectedPassword)
        );
    }
}
