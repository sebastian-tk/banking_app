package org.app;

import org.app.bank.Bank;

import static org.app.bank.Bank.*;

public class App {
    public static void main(String[] args) {
        final String path = "src/main/resources/";
        final String fileCustomers = "customers.json";
        final String fileBusinessCustomers = "businessCustomers.json";
        final String filePasswords = "passwords.json";
        final String fileTransactions = "transactionsHistory.json";

        try{
            Bank bank = createBank(path.concat(fileBusinessCustomers),path.concat(fileCustomers),path.concat(filePasswords),path.concat(fileTransactions));
            bank.service();
        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
        }
        System.out.println(" ### BANK CLOSED ###");
    }
}









