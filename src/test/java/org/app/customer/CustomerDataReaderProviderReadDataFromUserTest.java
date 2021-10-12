package org.app.customer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;
import java.util.function.Predicate;

import static org.app.customer.CustomerDataReaderProvider.readDataFromUser;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerDataReaderProviderReadDataFromUserTest {
    private static Predicate<String> predicateCorrect;
    private static Scanner scannerCorrect;
    private static String messageCorrect;


    @BeforeAll
    public static void init(){
        predicateCorrect = s -> false;
        scannerCorrect = new Scanner(System.in);
        messageCorrect ="message";
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when scanner argument is null")
    public void test1() {
        Scanner scannerTest = null;

        assertThatThrownBy(() -> readDataFromUser(scannerTest,messageCorrect,predicateCorrect))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scanner cannot be null in readDataFromUser");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException whennameDataargument is null")
    public void test2() {
        String messageTest = null;

        assertThatThrownBy(() -> readDataFromUser(scannerCorrect,messageTest,predicateCorrect))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid nameData argument in readDataFromUser");
    }


    @Test
    @DisplayName("should throws IllegalArgumentException whennameDataargument is null")
    public void test3() {
        String messageTest = "";

        assertThatThrownBy(() -> readDataFromUser(scannerCorrect,messageTest,predicateCorrect))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid nameData argument in readDataFromUser");
    }

    @Test
    @DisplayName("should throws IllegalArgumentException when pred argument is null")
    public void test4() {
        Predicate<String> predTest = null;

        assertThatThrownBy(() -> readDataFromUser(scannerCorrect,messageCorrect,predTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Interface Predicate can not be null in readDataFromUser");
    }

}
