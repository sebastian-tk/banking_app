package org.app.bank;

import org.app.customer.*;

import java.util.*;

public class Bank {
    private Map<Pesel, Customer> customersMap;
    private Map<Pesel, EncryptedPassword> hashPasswordsMap;

    private CustomersService customersService;

    private Bank(String fileNameBusinessCustomers,String fileNameCustomers, String fileNameEncryptedPasswords) {
        customersMap = new HashMap<>();
        hashPasswordsMap = new HashMap<>();

        customersService = CustomersService.createCustomersService(hashPasswordsMap,customersMap);
    }

    /**
     *
     * @param fileNameCustomers String as file name with customers
     * @param fileNameEncryptedPasswords String as file name with encrypted passwords
     * @return new object Bank
     */
    public static Bank createBank(String fileNameBusinessCustomers,String fileNameCustomers, String fileNameEncryptedPasswords){
        if(fileNameBusinessCustomers == null || fileNameBusinessCustomers.isEmpty()){
            throw new IllegalArgumentException("Invalid business customers file name when creating bank");
        }
        if(fileNameCustomers == null || fileNameCustomers.isEmpty()){
            throw new IllegalArgumentException("Invalid customers file name when creating bank");
        }
        if(fileNameEncryptedPasswords == null || fileNameEncryptedPasswords.isEmpty()){
            throw new IllegalArgumentException("Invalid passwords file name when creating bank");
        }
        return new Bank(fileNameBusinessCustomers,fileNameCustomers, fileNameEncryptedPasswords);
    }

}

