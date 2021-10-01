package org.app.customer;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.app.customer.CustomerDataReaderProvider.*;
import static org.app.customer.ValidatorPersonalData.*;


/*
    Pierwszy z nich to klient indywidualny, który opisywany jest przez imię, nazwisko, adres zamieszkania,
    adres email, numer telefonu oraz numer PESEL. Klient posiada jedno lub więcej kont bankowych, które opisywane są
    przez nazwę, unikalny numer oraz przez ilość środków, które na nich się znajdują.
 */
@EqualsAndHashCode
@ToString
@Getter
public class Customer implements ValidatorPersonalData,CustomerDataReaderProvider {
    private String name;
    private String surname;
    private Pesel pesel;
    private String address;
    private String email;
    private String phoneNumber;
    private Set<Account> accountSet;

    public Customer(Account accountNumber) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(">>>> NEW CUSTOMER <<<<");
        init(scanner);
        accountSet = Set.of(accountNumber);
    }

    protected Customer(String name, String surname, Pesel pesel, String address, String email, String phoneNumber, Set<Account> accountSet) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.address = address;
        this.email = email;

        this.phoneNumber = phoneNumber;
        this.accountSet = accountSet;
    }

    /**
     * @param name        String as name
     * @param surname     String as surname
     * @param pesel       String as number
     * @param address     String as address
     * @param email       String as email
     * @param phoneNumber String as phoneNumber
     * @param accountSet  reference HasSet<Account>
     * @return created new Customer
     */
    public static Customer createCustomer(String name, String surname, String pesel, String address, String email, String phoneNumber, Set<Account> accountSet) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name argument when create customer");
        }
        if (surname == null || surname.isEmpty()) {
            throw new IllegalArgumentException("Invalid surname argument when create customer");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Invalid address argument when create customer");
        }
        if (isAddressNotCorrect(address)) {
            throw new IllegalArgumentException("Incorrect address syntax: " + address);
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid email argument when create customer");
        }
        if (isEmailNotCorrect(email)) {
            throw new IllegalArgumentException("Incorrect email syntax: " + email);
        }
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Invalid phoneNumber argument when create customer");
        }
        if (isNumberNotCorrect(phoneNumber)) {
            throw new IllegalArgumentException("Incorrect phoneNumber syntax");
        }
        if (accountSet == null || accountSet.isEmpty()) {
            throw new IllegalArgumentException("Invalid accountSet argument when create customer");
        }
        return new Customer(name, surname, Pesel.createPesel(pesel), address, email, phoneNumber, accountSet);
    }

    public void service() {
        Scanner scanner = new Scanner(System.in);
        do {
            Account serveAccount = getAccountToService(scanner);
            menuAccount();
            switch (readChoice(scanner, 3)) {
                case 1 -> balanceAccount(serveAccount);
                case 2 -> {
                    boolean answerWithdraw = withdrawMoney(new BigDecimal(readAmountMoney(scanner)), serveAccount);
                    System.out.println(answerWithdraw ?"\t#Money withdrawn successfully;-)" :  "\t#Not enough money" );
                }
                case 3 -> {
                    depositMoney(new BigDecimal(readAmountMoney(scanner)), serveAccount);
                    System.out.println("\t#Money deposit successfully;-)");
                }
                default -> throw new IllegalStateException("\t#Error-unacceptable choice in service accounts");
            }
        } while (isYesOrNo("do you want to do any more activities"));
        System.out.println("\t\t### LOGOUT");
    }

    /**
     * @param serveAccount object Account
     *                     Method prints data account by pattern: numberAccount,amountMoney
     */
    private void balanceAccount(Account serveAccount) {
        System.out.println(serveAccount.getNumber() + ": " + serveAccount.getAmountMoney() + "zl");
    }

    /**
     * @param moneyOut object BigDecimal as money to withdraw
     * @param account  object Account
     * @return true, if the money was successfully paid out, else false
     */
    protected boolean withdrawMoney(BigDecimal moneyOut, Account account) {
        if (moneyOut == null || moneyOut.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid money argument when withdrawing");
        }
        if (account == null) {
            throw new IllegalArgumentException("Invalid account argument when withdrawing");
        }
        if (isThereNotEnoughMoney(moneyOut, account)) {
            return false;
        }
        account.subtractMoney(moneyOut);
        return true;
    }

    /**
     * @param nextMoney object BigDecimal with new money to add
     * @param account   object Account
     *                  Method add nextMoney to amountMoney of account
     */
    protected void depositMoney(BigDecimal nextMoney, Account account) {
        if (nextMoney == null || nextMoney.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid money argument when depositing");
        }
        if (account == null) {
            throw new IllegalArgumentException("Invalid account argument when depositing");
        }
        account.addMoney(nextMoney);
    }


    /**
     * @param moneyToWithdrawn object BigDecimal as money to withdraw
     * @param account          object Account to check account balance
     * @return true, if there is less money in account than money to withdraw, else false
     */
    private boolean isThereNotEnoughMoney(BigDecimal moneyToWithdrawn, Account account) {
        return account.getAmountMoney().compareTo(moneyToWithdrawn) < 0;
    }

    /**
     * Method prints menu to service account
     */
    private void menuAccount() {
        System.out.println("\t\t\t" + name + " " + surname);
        System.out.println("\t ****** MENU ACCOUNT *******");
        System.out.println("1. Check account balance");
        System.out.println("2. Withdraw money");
        System.out.println("3. Deposit money");
    }

    /**
     * @param scanner object Scanner
     * @return object Account chosen by the customer
     */
    private Account getAccountToService(Scanner scanner) {
        int amountAccount = accountSet.size();
        if (amountAccount == 1) {
            return convertAccountsToList().get(0);
        }
        System.out.println("\t>>>Select an account");
        List<Account> accountList = printNumbersAccount();
        int nrAccount = readChoice(scanner,amountAccount);
        return accountList.get(nrAccount - 1);
    }

    /**
     * @return List with Accounts from accountSet
     */
    private List<Account> convertAccountsToList() {
        return accountSet.stream().toList();
    }

    /**
     * @param scanner object Scanner to read data from user
     *                Method loads subsequent fields of the Customer class except Account
     */
    private void init(Scanner scanner) {
        this.name = readDataFromUser(scanner, "name: ", ValidatorPersonalData::isNameOrSurnameNotCorrect);
        this.surname = readDataFromUser(scanner, "surname: ", ValidatorPersonalData::isNameOrSurnameNotCorrect);
        this.pesel = Pesel.createPesel(readDataFromUser(scanner, "pesel: ", dataFromUser -> !Pesel.isPeselCorrect(dataFromUser)));
        this.address = readDataFromUser(scanner, "address: ", ValidatorPersonalData::isAddressNotCorrect);
        this.email = readDataFromUser(scanner, "email: ", ValidatorPersonalData::isEmailNotCorrect);
        this.phoneNumber = readDataFromUser(scanner, "phoneNumber: ", ValidatorPersonalData::isNumberNotCorrect);
    }

    /**
     * @return List with Accounts this customer and print this numbers
     */
    private List<Account> printNumbersAccount() {
        List<Account> accountsList = convertAccountsToList();
        int idx = 0;
        for (var element : accountsList) {
            idx++;
            System.out.println(idx + "." + element.getNumber());
        }
        return accountsList;
    }




}
