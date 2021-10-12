package org.app.customer;

import org.app.customer.extensions.ParametersResolverForCustomer;
import org.app.customer.extensions.ParametersResolverForCustomerService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.manipulation.Alphanumeric;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ParametersResolverForCustomerService.class)
@ExtendWith(ParametersResolverForCustomer.class)
public record CustomerServiceAddTest(CustomersService customersServiceCorrect, Customer customerCorrect) {
    private static final StringBuilder passwordCorrect = new StringBuilder("alaMaKota");

    @Test
    @DisplayName("should throws IllegalArgumentsException when customer argument is null")
    public void test1() {
        Customer customerTest = null;

        assertThatThrownBy(() -> customersServiceCorrect.add(customerTest, passwordCorrect))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customer argument when add");
    }

    @Test
    @DisplayName("should throws IllegalArgumentsException when password argument is null")
    public void test2() {
        StringBuilder passwordTest = null;

        assertThatThrownBy(() -> customersServiceCorrect.add(customerCorrect, passwordTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when add");
    }

    @Test
    @DisplayName("should throws IllegalArgumentsException when password argument is empty")
    public void test3() {
        StringBuilder passwordTest = new StringBuilder();

        assertThatThrownBy(() -> customersServiceCorrect.add(customerCorrect, passwordTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password argument when add");
    }

    @Test
    @DisplayName("should throws IllegalArgumentsException when customer already exist")
    public void test4() {
        Customer existCustomer = getCustomers().get(0);

        assertThatThrownBy(() -> customersServiceCorrect.add(existCustomer, passwordCorrect))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect customer:" + existCustomer.getName() + " " + existCustomer.getSurname() + ". Customer already exist");
    }
    private List<Customer> getCustomers() {
        var list = customersServiceCorrect.getMapCustomers().values().stream().toList();
        if (list.isEmpty()) {
            throw new IllegalStateException("Data corrupted-no customers to test");
        }
        return list;
    }

    @Test
    @DisplayName("should throw an IllegalArgumentsException when a customer with that pesel already exists")
    public void test5() {
        Customer existCustomer = getCustomers().get(0);
        Customer customerWithTheSamePesel = new Customer(customerCorrect.getName(),
                customerCorrect.getSurname(),
                existCustomer.getPesel(),
                customerCorrect.getAddress(),
                customerCorrect.getEmail(),
                customerCorrect.getPhoneNumber(),
                customerCorrect.getAccountSet());

        assertThatThrownBy(() -> customersServiceCorrect.add(customerWithTheSamePesel, passwordCorrect))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect pesel customer : " + customerWithTheSamePesel.getPesel().getNumber() + ". This pesel already exist");
    }

    @Test
    @DisplayName("should throw an IllegalArgumentsException when a customer with that numberAccount already exists")
    public void test6() {
        Customer existCustomer = getCustomers().get(0);
        Customer customerWithTheSameNumberAccount = new Customer(customerCorrect.getName(),
                customerCorrect.getSurname(),
                customerCorrect.getPesel(),
                customerCorrect.getAddress(),
                customerCorrect.getEmail(),
                customerCorrect.getPhoneNumber(),
                existCustomer.getAccountSet());

        assertThatThrownBy(() -> customersServiceCorrect.add(customerWithTheSameNumberAccount, passwordCorrect))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect number account: " + customerWithTheSameNumberAccount.getAccountSet() + ". This account already exist");
    }

    @Test
    @DisplayName("should be new customer in CustomerService when data correct")
    public void test7() {
        customersServiceCorrect.add(customerCorrect,passwordCorrect);

        assertAll(
                "Map serviceCustomers test",
                ()->assertTrue(customersServiceCorrect.getMapCustomers().containsKey(customerCorrect.getPesel())),
                ()->assertTrue(customersServiceCorrect.getMapCustomers().containsValue(customerCorrect))
        );
    }
}
