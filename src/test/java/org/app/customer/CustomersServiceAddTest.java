package org.app.customer;

import org.app.customer.transaction.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomersServiceAddTest {
    private static Customer testCustomer;
    private static Customer addedCustomer;
    private static Pesel peselObj;
    private static Pesel peselAdded;
    private static EncryptedPassword passwordObj;
    private static char[] passwordChars;
    private static String allPasswordHash;

    @BeforeAll
    public static void init(){
        String name = "name";
        String surname = "surname";
        String email = "firstword.secondword@gmail.com";
        String phoneNumber = "111-111-111";
        String address = "ul. Adama Mickiewicza 10/23 10-100 Warszawa";
        String pesel = "78092475727";
        String pesel2 = "84091146348";
        Set<Account> accountSet = Set.of(Account.createAccount("Personal",new BigInteger("12345678901234567890123456"),new BigDecimal("0")));
        Set<Account> accountSet2 = Set.of(Account.createAccount("Personal",new BigInteger("32145678901234567890123456"),new BigDecimal("0")));
        peselObj = Pesel.createPesel(pesel);
        peselAdded = Pesel.createPesel(pesel2);
        passwordChars = "CDB2A9945595EDAD9C892A2F09610B58".toCharArray();
        allPasswordHash = "CDB2A9945595EDAD9C892A2F09610B58:CDB2A9945595EDAD9C892A2F09610B58";

        testCustomer =Customer.createCustomer(name, surname, pesel, address, email, phoneNumber, accountSet);
        addedCustomer =Customer.createCustomer(name, surname, pesel2, address, email, phoneNumber, accountSet2);
        passwordObj = EncryptedPassword.createEncryptedPassword(peselObj,allPasswordHash);
        
    }


    @Test
    @DisplayName("should throws IllegalArgumentsException when customer argument is null")
    public void test1(){
        Customer customer = null;
        Map<Pesel,EncryptedPassword> mapHashPasswords = new HashMap<>(Map.of(peselObj,passwordObj));
        Map<Pesel,Customer> customerMap = new HashMap<>(Map.of(peselObj, testCustomer));
        Map<Pesel, List<Transaction>> transactionsMap = new HashMap<>();


        CustomersService CustomersServiceTest = CustomersService.createCustomersService(mapHashPasswords, customerMap,transactionsMap);

        assertThatThrownBy(()-> CustomersServiceTest.add(customer,passwordChars))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customer argument when add");
    }

    @Test
    @DisplayName("should no throw when customer is correct argument")
    public void test2(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = new HashMap<>(Map.of(peselObj,passwordObj));
        Map<Pesel,Customer> customerMap = new HashMap<>(Map.of(peselObj, testCustomer));
        Map<Pesel, List<Transaction>> transactionsMap = new HashMap<>();

        CustomersService CustomersServiceTest = CustomersService
                                                .createCustomersService(mapHashPasswords, customerMap,transactionsMap);

        assertThatCode(()->CustomersServiceTest.add(addedCustomer,passwordChars)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("should throws IllegalArgumentsException when add customer with exist number account")
    public void test3(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = new HashMap<>(Map.of(peselObj,passwordObj));
        Map<Pesel,Customer> customerMap = new HashMap<>(Map.of(peselObj, testCustomer));
        Map<Pesel, List<Transaction>> transactionsMap = new HashMap<>();

        CustomersService CustomersServiceTest = CustomersService.createCustomersService(mapHashPasswords,customerMap,transactionsMap);

        assertThatThrownBy(()->CustomersServiceTest.add(testCustomer,passwordChars))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Incorrect data of the user with the pesel number:" + testCustomer.getPesel().getNumber()+". Data exist");
    }

    @Test
    @DisplayName("should map has new customer after correct add")
    public void test4(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = new HashMap<>(Map.of(peselObj,passwordObj));
        Map<Pesel,Customer> customerMap = new HashMap<>(Map.of(peselObj, testCustomer));
        Map<Pesel, List<Transaction>> transactionsMap = new HashMap<>();

        Map<Pesel,Customer> customerMapExpected = new HashMap<> (Map.copyOf(customerMap));
        customerMapExpected.put(peselAdded,addedCustomer);

        CustomersService CustomersServiceTestExpected = CustomersService.createCustomersService(mapHashPasswords,customerMap,transactionsMap);
        CustomersServiceTestExpected.add(addedCustomer, passwordChars);

        assertEquals(customerMapExpected,CustomersServiceTestExpected.getMapCustomers());
    }
}
