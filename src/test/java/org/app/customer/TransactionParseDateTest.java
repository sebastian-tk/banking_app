package org.app.customer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.app.customer.transaction.Transaction.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TransactionParseDateTest {
    @Test
    @DisplayName("should throws IllegalArgumentException when date argument is null")
    public void test1() {
        String dateTest = null;

        assertThatThrownBy(() -> parseDate(dateTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid date argument when parse date");
    }
    @Test
    @DisplayName("should throws IllegalArgumentException when date argument is empty")
    public void test2() {
        String dateTest = "";

        assertThatThrownBy(() -> parseDate(dateTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid date argument when parse date");
    }

    @Test
    @DisplayName("should return object LocalDate complies with input argument")
    public void test3() {
        LocalDate expectedDate = LocalDate.of(2001,1,1);
        String dateTest = "2001-01-01";

        LocalDate resultDate = parseDate(dateTest);

        Assertions.assertAll(
                "Test return object of parseDate",
                ()-> assertThat(resultDate.getYear()).isEqualTo(expectedDate.getYear()),
                ()-> assertThat(resultDate.getMonthValue()).isEqualTo(expectedDate.getMonthValue()),
                ()-> assertThat(resultDate.getDayOfMonth()).isEqualTo(expectedDate.getDayOfMonth())
        );
    }

}
