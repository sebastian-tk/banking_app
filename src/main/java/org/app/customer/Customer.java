package org.app.customer;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.app.customer.EncryptedPassword.convertToChars;
import static org.app.customer.ValidatorPersonalData.*;


/*
    Pierwszy z nich to klient indywidualny, który opisywany jest przez imię, nazwisko, adres zamieszkania,
    adres email, numer telefonu oraz numer PESEL. Klient posiada jedno lub więcej kont bankowych, które opisywane są
    przez nazwę, unikalny numer oraz przez ilość środków, które na nich się znajdują.
 */
@EqualsAndHashCode
@ToString
@Getter
public class Customer implements ValidatorPersonalData {
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

    public void service(String hashPassword, byte[] salt) {
        Scanner scanner = new Scanner(System.in);
        if (login(hashPassword, salt)) {
            do {
                Account serveAccount = getAccountToService(scanner);
                menuAccount();
                switch (readChoice(scanner, generateNumber(1, 3))) {
                    case 1 -> balanceAccount(serveAccount);
                    case 2 -> {
                        boolean answerWithdraw = withdrawMoney(new BigDecimal(readAmountMoney(scanner)), serveAccount);
                        System.out.println(answerWithdraw ? "\t#Not enough money" : "\t#Money withdrawn successfully;-)");
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
    }

    /**
     * @param hashPassword String as hashPassword
     * @param salt         array bytes as salt
     * @return true if customer login was successful, false otherwise
     */
    private boolean login(String hashPassword, byte[] salt) {
        if (hashPassword == null || hashPassword.isEmpty()) {
            throw new IllegalArgumentException("Invalid hashPassword argument in service");
        }
        if (salt == null || salt.length == 0) {
            throw new IllegalArgumentException("Invalid salt argument in service");
        }
        final int NUMBER_LOGIN_ATTEMPTS = 3;
        Scanner scanner = new Scanner(System.in);
        boolean answer = false;
        int counter = 0;
        char[] bufferPassword;
        do {
            bufferPassword = convertToChars(readExpression(scanner, "enter password: "));
            if (areEquals(hashPassword, EncryptedPassword.generatePasswordHash(bufferPassword, salt), String::equals)) {
                System.out.println("\t Successful login  ;-)\n");
                answer = true;
            } else {
                counter++;
                System.out.println("\t# Invalid password");
            }
        } while (!answer && counter != NUMBER_LOGIN_ATTEMPTS);
        if (counter == NUMBER_LOGIN_ATTEMPTS) {
            System.out.println("\t### ACCESS DENIED ###");
        }
        return answer;
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
     * @param scanner object Scanner
     * @return String as positive double value
     */
    private String readAmountMoney(Scanner scanner) {
        return readDataFromUser(scanner, "amount money: ", Account::isDecimalNotPositiveValue);
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
        int nrAccount = readChoice(scanner, generateNumber(1, amountAccount));
        return accountList.get(nrAccount - 1);
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

    /**
     * @param question String as question for user
     * @return true, if user answered yes or false if user answered false
     */
    private boolean isYesOrNo(String question) {
        Scanner scanner = new Scanner(System.in);
        String buffer;
        do {
            buffer = readExpression(scanner, question + "(yes/no):");
            if (!areEquals(buffer, "yes", String::equals) && !areEquals(buffer, "no", String::equals)) {
                System.out.println("\t# Unavailable answer");
                buffer = "";
            }
        } while (buffer.isEmpty());
        return areEquals(buffer, "yes", String::equals);
    }

    /**
     * @param minRange minimum range
     * @param maxRange maximum range
     * @return List<Integer> with numbers between minRange and maxRange
     */
    private List<Integer> generateNumber(int minRange, int maxRange) {
        if (minRange > maxRange) {
            throw new IllegalArgumentException("Invalid range arguments");
        }
        return IntStream.rangeClosed(minRange, maxRange).boxed().toList();
    }

    /**
     * @param scanner           object Scanner
     * @param acceptableChoices List<Integer>
     * @return integer value as an acceptable number selected by the user
     */
    private int readChoice(Scanner scanner, List<Integer> acceptableChoices) {
        int bufferChoice;
        do {
            System.out.print("choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("\t#incorrect value");
                scanner.nextLine();
            }
            bufferChoice = parseToInt(scanner.nextLine());
            if (!acceptableChoices.contains(bufferChoice)) {
                System.out.println("\tThere is no such choice");
                bufferChoice = 0;
            }
        } while (bufferChoice == 0);
        return bufferChoice;
    }

    /**
     * @param value String as value to parse
     * @return integer value as parsed integer from value
     */
    private int parseToInt(String value) {
        return Integer.parseInt(value);
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
     * @param scanner  object Scanner
     * @param nameData String as name of data to read
     * @param pred     interface Predicate
     * @return correct String from user
     */
    private String readDataFromUser(Scanner scanner, String nameData, Predicate<String> pred) {
        String buffer;
        boolean run;
        do {
            buffer = readExpression(scanner, "enter " + nameData);
            run = pred.test(buffer);
            if (run) {
                System.out.println("\t#Invalid " + nameData);
            }
        } while (run);
        return buffer;
    }

    /**
     * @param scanIn  object Scanner
     * @param message String as message to user
     * @return String as expression from user
     */
    private String readExpression(Scanner scanIn, String message) {
        System.out.print(message);
        return scanIn.nextLine();
    }

    /**
     * @param firstObj  second object to compare
     * @param secondObj first object to compare
     * @return true, if firstObj is equal with secondObj, else false
     */
    private <T> boolean areEquals(T firstObj, T secondObj, BiPredicate<T, T> pred) {
        return pred.test(firstObj, secondObj);
    }
}
