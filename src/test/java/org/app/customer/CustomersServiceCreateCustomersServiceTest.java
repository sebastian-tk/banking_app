package org.app.customer;

import org.app.customer.transaction.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.app.customer.CustomersService.*;
import static org.app.customer.transaction.Transaction.*;
import static org.app.customer.transaction.TransactionType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomersServiceCreateCustomersServiceTest {
    @Mock
    private Customer customerTest;
    @Mock
    private Pesel peselObj;
    @Mock
    private EncryptedPassword passwordObj;
    @Mock
    private Account accountTest;


    @Test
    @DisplayName("should throws IllegalArgumentsException when mapHashPasswords argument is null")
    public void test1(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = null;
        Map<Pesel,Customer> customersMap = Map.of(peselObj,customerTest);
        Map<Pesel, List<Transaction>> transactionsMap = new HashMap<>();
    
        Assertions.assertThatThrownBy(()-> createCustomersService(mapHashPasswords,customersMap,transactionsMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid mapHashPasswords argument");
    }

    @Test
    @DisplayName("should throws IllegalArgumentsException when customers argument is null")
    public void test2(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = Map.of(peselObj,passwordObj);
        Map<Pesel,Customer> customerMap = null;
        Map<Pesel, List<Transaction>> transactionsMap = new HashMap<>();
        
        Assertions.assertThatThrownBy(()-> createCustomersService(mapHashPasswords,customerMap,transactionsMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customers argument");
    }


    @Test
    @DisplayName("should throws IllegalArgumentsException when transactionsMap argument is null")
    public void test3(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = Map.of(peselObj,passwordObj);
        Map<Pesel,Customer> customerMap = Map.of(peselObj,customerTest);
        Map<Pesel, List<Transaction>> transactionsMap = null;

        Assertions.assertThatThrownBy(()-> createCustomersService(mapHashPasswords,customerMap,transactionsMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid map transactions argument");
    }

    @Test
    @DisplayName("should return object ServiceCustomer the same as passed argument")
    public void test4() {
        Map<Pesel, EncryptedPassword> mapHashPasswordsExpected = Map.of(peselObj, passwordObj);
        Map<Pesel, Customer> customerMapExpected = Map.of(peselObj, customerTest);
        String date = "2021-02-10";
        Map<Pesel, List<Transaction>> transactionsMapExpected = Map.of(peselObj,List.of(createTransaction(peselObj,
                                                                                        DEPOSIT,
                                                                                        new BigDecimal("1.0"),
                                                                                        date,
                                                                                        accountTest)));
    
        CustomersService CustomersServiceTest = createCustomersService(mapHashPasswordsExpected,customerMapExpected,transactionsMapExpected);

        assertAll(
                "Test CustomersService object",
                ()->assertThat(CustomersServiceTest).isNotNull(),
                ()->assertEquals(mapHashPasswordsExpected,CustomersServiceTest.getMapHashPasswords()),
                ()->assertEquals(customerMapExpected,CustomersServiceTest.getMapCustomers()),
                ()->assertEquals(transactionsMapExpected,CustomersServiceTest.getMapTransactions())
        );
    }
}
