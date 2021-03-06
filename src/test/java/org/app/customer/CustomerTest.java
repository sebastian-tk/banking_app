package org.app.customer;

import org.app.persistence.converter.CustomersJsonConverter;
import org.app.persistence.model.CustomersList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CustomerTest {
    private static final String fileName= "src/test/resources/customerCorrectInputData.json";
    private static Customer correctCustomer;
    private static String correctName;
    private static String correctSurname;
    private static Pesel correctPesel;
    private static String correctAddress;
    private static String correctEmail;
    private static String correctPhoneNumber;
    private static Set<Account> correctAccountSet;

    @BeforeAll
    public static void init(){
        correctCustomer = readDataCustomer();
        correctName = correctCustomer.getName();
        correctSurname = correctCustomer.getSurname();
        correctPesel = correctCustomer.getPesel();
        correctAddress = correctCustomer.getAddress();
        correctEmail = correctCustomer.getEmail();
        correctPhoneNumber = correctCustomer.getPhoneNumber();
        correctAccountSet = correctCustomer.getAccountSet();
    }

    private static Customer readDataCustomer(){
        CustomersJsonConverter customersJsonConverter = new CustomersJsonConverter(fileName);
        Optional<CustomersList> customersList = customersJsonConverter.fromJson();
        if(customersList.isEmpty()){
            throw new IllegalStateException("Data corrupted from test Customer file");
        }
        return customersList
                .stream()
                .map(CustomersList::getCustomers)
                .flatMap(Collection::stream)
                .toList()
                .get(0);
    }


    @Test
    @DisplayName("should throws IllegalArgumentExceptions when name argument is null")
    public void test1() {
        String nameTest = null;
        assertThatThrownBy(() -> new Customer(nameTest, correctSurname, correctPesel, correctAddress, correctEmail, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name argument when create customer");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when name argument is empty")
    public void test2() {
        String nameTest = "";

        assertThatThrownBy(() -> new Customer(nameTest, correctSurname, correctPesel, correctAddress, correctEmail, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid name argument when create customer");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when surname argument is null")
    public void test3() {

        String surnameTest = null;

        assertThatThrownBy(() -> new Customer(correctName, surnameTest, correctPesel, correctAddress, correctEmail, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid surname argument when create customer");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when surname argument is empty")
    public void test4() {
        String surnameTest = "";

        assertThatThrownBy(() -> new Customer(correctName, surnameTest, correctPesel, correctAddress, correctEmail, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid surname argument when create customer");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when address argument is null")
    public void test5() {

        String addressTest = null;

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, addressTest, correctEmail, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid address argument when create customer");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when address argument is empty")
    public void test6() {
        String addressTest = "";

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, addressTest, correctEmail, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid address argument when create customer");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentExceptions when address is invalid")
    @ValueSource(strings = {
            "ul.Adama Mickiewicza 10/23 10-100 Warszawa",
            "Adama Mickiewicza 10/23 10-100 Warszawa",
            "ul. AdamA Mickiewicza 10/23 10-100 Warszawa",
            "ul. Adama Mickiewicza 10/b 10-100 Warszawa",
            "ul. Adama Mickiewicza 10 100-100 Warszawa",
            "ul. Adama Mickiewicza  10-100 Warszawa",
            "ul. Adama Mickiewicza 10 10-10 Warszawa",
            "ul. Adama Mickiewicza 10 10-100 "
    })
    public void test7(String addressesTest) {

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, addressesTest, correctEmail, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect address syntax: "+addressesTest);
    }


    @ParameterizedTest
    @DisplayName("should not throws when address is correct")
    @ValueSource(strings = {
            "ul. Schmitta 23 10-100 Warszawa",
            "ul. Adama Mickiewicza 13 10-100 Warszawa",
            "ul. Adama Mickiewicza 10/23 10-100 Warszawa",
            "ul. Romualda Traugutta 10 10-100 Warszawa",
    })
    public void test8(String addressesTest) {

        assertThatCode(() -> new Customer(correctName, correctSurname, correctPesel, addressesTest, correctEmail, correctPhoneNumber, correctAccountSet))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when email argument is null")
    public void test9() {
        String emailTest = null;

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, emailTest, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email argument when create customer");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when email argument is empty")
    public void test10() {
        String emailTest = "";

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, emailTest, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email argument when create customer");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when email syntax is incorrect")
    @CsvFileSource(resources = "/incorrectEmails.csv")
    public void test11(String emailsTest) {

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, emailsTest, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect email syntax: "+emailsTest);
    }

    @ParameterizedTest
    @DisplayName("should no throws when address syntax is correct")
    @CsvFileSource(resources = "/correctEmails.csv")
    public void test12(String emailsTest) {

        assertThatCode(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, emailsTest, correctPhoneNumber, correctAccountSet))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when number argument is null")
    public void test13() {
        String phoneNumberTest = null;

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, correctEmail, phoneNumberTest, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid phoneNumber argument when create customer");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when number argument is empty")
    public void test14() {
        String phoneNumberTest = "";

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, correctEmail, phoneNumberTest, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid phoneNumber argument when create customer");
    }

    @ParameterizedTest
    @DisplayName("should throws IllegalArgumentException when phoneNumber syntax is incorrect")
    @ValueSource(strings = {
            "12-123-234",
            "123-13-134",
            "123-453-34",
            "123_456_789",
            "23d-456-789"
    })
    public void test15(String numbersTest) {

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, correctEmail, numbersTest, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect phoneNumber syntax");
    }

    @Test
    @DisplayName("should not throws exception when phoneNumber syntax is correct")
    public void test16() {

        assertThatCode(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, correctEmail, correctPhoneNumber, correctAccountSet))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when accountSet argument is null")
    public void test17() {
        Set<Account> accountSetTest = null;

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, correctEmail, correctPhoneNumber, accountSetTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid accountSet argument when create customer");
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when accountSet argument is empty")
    public void test18() {
        Set<Account> accountSetTest = new HashSet<>();

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, correctPesel, correctAddress, correctEmail, correctPhoneNumber, accountSetTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid accountSet argument when create customer");
    }

    @Test
    @DisplayName("should return object Customer the same as passed by argument")
    public void test19() {

        Customer customerResult = new Customer(correctName, correctSurname, correctPesel, correctAddress, correctEmail, correctPhoneNumber, correctAccountSet);

        assertAll(
                "Customer fields test",
                ()-> assertThat(customerResult).isNotNull(),
                ()-> assertThat(customerResult.getName()).isEqualTo(correctName),
                ()-> assertThat(customerResult.getSurname()).isEqualTo(correctSurname),
                ()-> assertThat(customerResult.getPesel()).isEqualTo(correctPesel),
                ()-> assertThat(customerResult.getAddress()).isEqualTo(correctAddress),
                ()-> assertThat(customerResult.getEmail()).isEqualTo(correctEmail),
                ()-> assertThat(customerResult.getPhoneNumber()).isEqualTo(correctPhoneNumber),
                ()-> assertThat(customerResult.getAccountSet()).isEqualTo(correctAccountSet)
                );
    }

    @Test
    @DisplayName("should throws IllegalArgumentExceptions when pesel argument is null")
    public void test20() {
        Pesel peselTest = null;

        assertThatThrownBy(() -> new Customer(correctName, correctSurname, peselTest, correctAddress, correctEmail, correctPhoneNumber, correctAccountSet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid pesel argument when create customer");
    }
}
