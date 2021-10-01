package org.app.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CustomersServiceCreateCustomersServiceTest {
    private static Customer customerTest;
    private static Pesel peselObj;
    private static EncryptedPassword passwordObj;

    @BeforeAll
    public static void init(){
        String name = "name";
        String surname = "surname";
        String email = "firstword.secondword@gmail.com";
        String phoneNumber = "111-111-111";
        String address = "ul. Adama Mickiewicza 10/23 10-100 Warszawa";
        String pesel = "78092475727";
        Set<Account> accountSet = Set.of(Account.createAccount("ING",new BigInteger("12345678901234567890123456"),new BigDecimal("0")));

        peselObj = Pesel.createPesel(pesel);
        customerTest =Customer.createCustomer(name, surname, pesel, address, email, phoneNumber, accountSet);
        passwordObj = EncryptedPassword.createEncryptedPassword(peselObj,"CDB2A9945595EDAD9C892A2F09610B58:CDB2A9945595EDAD9C892A2F09610B58");
        
    }

    @Test
    @DisplayName("should throws IllegalArgumentsException when mapHashPasswordss argument is null")
    public void test1(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = null;
        Map<Pesel,Customer> customersMap = Map.of(peselObj,customerTest);
    
        Assertions.assertThatThrownBy(()-> CustomersService.createCustomersService(mapHashPasswords,customersMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid mapHashPasswords argument");
    }

    @Test
    @DisplayName("should throws IllegalArgumentsException when customers argument is null")
    public void test2(){
        Map<Pesel,EncryptedPassword> mapHashPasswords = Map.of(peselObj,passwordObj);
        Map<Pesel,Customer> customerMap = null;
        
        Assertions.assertThatThrownBy(()-> CustomersService.createCustomersService(mapHashPasswords,customerMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customers argument");
    }


    @Test
    @DisplayName("should return object ServiceCustomer the same as passed argument")
    public void test3(){
        Map<Pesel,EncryptedPassword> mapHashPasswordsExpected = Map.of(peselObj,passwordObj);
        Map<Pesel,Customer> customerMapExpected = Map.of(peselObj,customerTest);
    
        CustomersService CustomersServiceTest = CustomersService.createCustomersService(mapHashPasswordsExpected,customerMapExpected);

        assertAll(
                "Test CustomersService object",
                ()->assertThat(CustomersServiceTest).isNotNull(),
                ()->assertEquals(mapHashPasswordsExpected,CustomersServiceTest.getMapHashPasswords()),
                ()->assertEquals(customerMapExpected,CustomersServiceTest.getMapCustomers())
        );
    }

}
