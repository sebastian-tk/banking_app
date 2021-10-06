package org.app.customer;

import org.app.customer.transaction.Transaction;
import org.app.customer.transaction.TransactionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CustomersServiceCreateCustomersServiceTest {
    private static Customer customerTest;
    private static Pesel peselObj;
    private static EncryptedPassword passwordObj;
    private static Account accountTest;

    @BeforeAll
    public static void init(){
        String name = "name";
        String surname = "surname";
        String email = "firstword.secondword@gmail.com";
        String phoneNumber = "111-111-111";
        String address = "ul. Adama Mickiewicza 10/23 10-100 Warszawa";
        String pesel = "78092475727";
        accountTest = Account.createAccount("ING",new BigInteger("12345678901234567890123456"),new BigDecimal("0"));
        Set<Account> accountSet = Set.of(accountTest);

        peselObj = Pesel.createPesel(pesel);
        customerTest =Customer.createCustomer(name, surname, pesel, address, email, phoneNumber, accountSet);
        passwordObj = EncryptedPassword.createEncryptedPassword(peselObj,"CDB2A9945595EDAD9C892A2F09610B58:CDB2A9945595EDAD9C892A2F09610B58");
        
    }

    @Test
    @DisplayName("should throws IllegalArgumentsException when mapHashPasswords argument is null")
    public void test1(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = null;
        Map<Pesel,Customer> customersMap = Map.of(peselObj,customerTest);
        Map<Pesel, List<Transaction>> transactionsMap = new HashMap<>();
    
        Assertions.assertThatThrownBy(()-> CustomersService.createCustomersService(mapHashPasswords,customersMap,transactionsMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid mapHashPasswords argument");
    }

    @Test
    @DisplayName("should throws IllegalArgumentsException when customers argument is null")
    public void test2(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = Map.of(peselObj,passwordObj);
        Map<Pesel,Customer> customerMap = null;
        Map<Pesel, List<Transaction>> transactionsMap = new HashMap<>();
        
        Assertions.assertThatThrownBy(()-> CustomersService.createCustomersService(mapHashPasswords,customerMap,transactionsMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customers argument");
    }


    @Test
    @DisplayName("should throws IllegalArgumentsException when transactionsMap argument is null")
    public void test3(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = Map.of(peselObj,passwordObj);
        Map<Pesel,Customer> customerMap = Map.of(peselObj,customerTest);
        Map<Pesel, List<Transaction>> transactionsMap = null;

        Assertions.assertThatThrownBy(()-> CustomersService.createCustomersService(mapHashPasswords,customerMap,transactionsMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid map transactions argument");
    }

    @Test
    @DisplayName("should return object ServiceCustomer the same as passed argument")
    public void test4() {
        Map<Pesel, EncryptedPassword> mapHashPasswordsExpected = Map.of(peselObj, passwordObj);
        Map<Pesel, Customer> customerMapExpected = Map.of(peselObj, customerTest);
        String date = "2021-02-10";
        Map<Pesel, List<Transaction>> transactionsMapExpected = Map.of(peselObj,List.of(Transaction.createTransaction(peselObj,
                                                                                TransactionType.DEPOSIT,
                                                                                new BigDecimal("1.0"),
                                                                                date,
                                                                                accountTest)));
    
        CustomersService CustomersServiceTest = CustomersService.createCustomersService(mapHashPasswordsExpected,customerMapExpected,transactionsMapExpected);

        assertAll(
                "Test CustomersService object",
                ()->assertThat(CustomersServiceTest).isNotNull(),
                ()->assertEquals(mapHashPasswordsExpected,CustomersServiceTest.getMapHashPasswords()),
                ()->assertEquals(customerMapExpected,CustomersServiceTest.getMapCustomers()),
                ()->assertEquals(transactionsMapExpected,CustomersServiceTest.getMapTransactions())
        );
    }

}
