package org.app.customer;

import java.util.Map;
import java.util.Scanner;

public class CustomersService {
    private final Scanner scanner;
    private Map<Pesel,EncryptedPassword> mapHashPasswords;
    private Map<Pesel,Customer> mapCustomers;
    private Customer serveCustomer;
    private Account serveAccount;


    private CustomersService(Map<Pesel,EncryptedPassword> mapHashPasswords, Map<Pesel,Customer>customers) {
        this.mapHashPasswords = mapHashPasswords;
        this.mapCustomers = customers;
        this.serveCustomer = null;
        this.serveAccount = null;
        this.scanner = new Scanner(System.in);
    }

    /**
     * @param mapHashPasswords object Map<Pesel,EncryptedPassword>
     * @param customers     object Map<Pesel,Customer>
     * @return new object serviceCustomer
     */
    public static CustomersService createCustomersService(Map<Pesel,EncryptedPassword> mapHashPasswords, Map<Pesel,Customer> customers) {
        if (mapHashPasswords == null) {
            throw new IllegalArgumentException("Invalid mapHashPasswords argument");
        }
        if (customers == null ) {
            throw new IllegalArgumentException("Invalid customers argument");
        }
        return new CustomersService(mapHashPasswords, customers);
    }
    public Map<Pesel, EncryptedPassword> getMapHashPasswords() {
        return mapHashPasswords;
    }

    public Map<Pesel,Customer> getMapCustomers() {
        return mapCustomers;
    }

}
