package org.app.customer;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.app.customer.transaction.Transaction;
import org.app.customer.transaction.TransactionType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.app.customer.Account.*;
import static org.app.customer.CustomerDataReaderProvider.*;
import static org.app.customer.ValidatorPersonalData.*;
import static org.app.customer.transaction.Transaction.*;
import static org.app.customer.transaction.TransactionType.*;


/*
    Pierwszy z nich to klient indywidualny, który opisywany jest przez imię, nazwisko, adres zamieszkania,
    adres email, numer telefonu oraz numer PESEL. Klient posiada jedno lub więcej kont bankowych, które opisywane są
    przez nazwę, unikalny numer oraz przez ilość środków, które na nich się znajdują.
 */
@EqualsAndHashCode
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

    @Override
    public String toString() {
        return  "\n\t\t\t\t" + name + " " + surname + "\n"+
                "\t\t\t\t" +(this.getClass().getSimpleName());

    }

    /**
     *
     * @param transactions List with user Transactions
     * @return  modified transactions as List<Transactions>
     */
    public List<Transaction> service(List<Transaction> transactions) {
        if(transactions == null){
            throw new IllegalArgumentException("Invalid transactions argument in service of customer");
        }

        Scanner scanner = new Scanner(System.in);
        var mapTransactions = groupByNumberAccount(transactions);

        do {
            var serveAccount = getAccountToService(scanner);
            var transactionAccount = getTransactionOfAccount(mapTransactions,serveAccount);

                do{
                    menuAccount(serveAccount);
                    switch (readChoice(scanner, 4)) {
                        case 1 -> balanceAccount(serveAccount);
                        case 2 -> serviceWithdraw(scanner, serveAccount, transactionAccount);
                        case 3 -> serviceDeposit(scanner, serveAccount, transactionAccount);
                        case 4 -> serviceHistorySearching(scanner, transactionAccount);
                        default -> throw new IllegalStateException("\t#Error-unacceptable choice in service accounts");
                    }
                }while (isYesOrNo("do you want to do any operations in this account"));

                mapTransactions.put(serveAccount.getNumber(),transactionAccount);
        } while (accountSet.size()==1 && isYesOrNo("do you want to do any more activities"));
        System.out.println("\t\t### LOGOUT");
        return getAllTransactions(mapTransactions);
    }

    /**
     * @param serveAccount object Account
     *                     Method prints data account by pattern: numberAccount,amountMoney
     */
    private void balanceAccount(Account serveAccount) {
        System.out.println(serveAccount.getNumber() + ": " + serveAccount.getAmountMoney() + "zl");
    }

    /**
     *
     * @param scanner object Scanner
     * @param serveAccount object Account to support
     * @param transactionAccount object List<Transaction> with all transactions from serveAccount
     */
    protected void serviceWithdraw(Scanner scanner,Account serveAccount,List<Transaction> transactionAccount){
        BigDecimal moneyWithdraw = new BigDecimal(readAmountMoney(scanner));
        boolean answerWithdraw = withdrawMoney(moneyWithdraw, serveAccount);
        if(answerWithdraw){
            saveTransaction(moneyWithdraw,WITHDRAW,transactionAccount,serveAccount);
            System.out.println("\t#Money withdrawn successfully;-)");
        }else{
            System.out.println("\t#Not enough money");
        }
    }

    /**
     *
     * @param scanner object Scanner
     * @param serveAccount object Account to support
     * @param transactionAccount object List<Transaction> with all transactions from serveAccount
     */
    protected void serviceDeposit(Scanner scanner,Account serveAccount,List<Transaction> transactionAccount){
        BigDecimal money = new BigDecimal(readAmountMoney(scanner));
        depositMoney(money, serveAccount);
        saveTransaction(money,DEPOSIT,transactionAccount,serveAccount);
        System.out.println("\t#Money deposit successfully;-)");
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
     *
     * @param money object BigDecimal as money
     * @param type object TransactionType
     * @param transactions  object List with Transactions
     * @param servedAccount object Account
     */
    private void saveTransaction(BigDecimal money, TransactionType type, List<Transaction> transactions, Account servedAccount) {
        transactions.add(createTransaction(
                pesel,
                type,
                money,
                LocalDate.now().toString(),
                createAccount(servedAccount.getName()
                        , servedAccount.getNumber()
                        , servedAccount.getAmountMoney())));
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
     *
     * @param account   object Account to serve
     *                   Method prints menu to service account
     */
    private void menuAccount(Account account) {
        System.out.println(this);
        System.out.println("account: "+account.getNumber());
        System.out.println("\t ****** MENU ACCOUNT *******");
        System.out.println("1. Check account balance");
        System.out.println("2. Withdraw money");
        System.out.println("3. Deposit money");
        System.out.println("4. History transactions");
    }

    /**
     *
     * @param scanner object Scanner
     * @param transactionsAccount   List with Transactions of account
     *                              The method allows you to view and search the history of payments for a
     *                              given account number.
     */
    private void serviceHistorySearching(Scanner scanner, List<Transaction> transactionsAccount){
        menuSearching();
        switch (readChoice(scanner, 4)) {
            case 1 -> {
                System.out.println("Enter date by pattern yyyy-MM-dd");
                String readDate =readDataFromUser(scanner, "date: ", Transaction::isDateNotCorrect);
                printByDate(transactionsAccount,transaction -> transaction.getDate().equals(readDate));
            }
            case 2 -> {
                BigDecimal searchMoney = new BigDecimal(readAmountMoney(scanner));
                printByDate(transactionsAccount,transaction ->  transaction
                                                                .getMoney()
                                                                .equals(searchMoney));
            }
            case 3 -> {
                System.out.println("Enter type of transaction ");
                String nameType =readDataFromUser(scanner, "type: ", TransactionType::isTypeNotCorrect);
                printByDate(transactionsAccount,transaction -> transaction.getType().name().equals(nameType));
            }
            case 4 -> printByDate(transactionsAccount,transaction -> true);
            default -> throw new IllegalStateException("\t#Error-unacceptable choice in service history searching");
        }
    }

    /**
     * Method prints menu to searching history transactions
     */
    private void menuSearching() {
        System.out.println(this);
        System.out.println("**** HISTORY SEARCH MENU ****");
        System.out.println(" Search by: ");
        System.out.println(" 1. date");
        System.out.println(" 2. amount of money");
        System.out.println(" 3. transaction type");
        System.out.println(" 4. all transactions");
    }

    /**
     *
     * @param transactionsAccount List with Transactions
     * @param pred  reference interface Predicate
     *              Method prints data from transactionAccount according to pred
     */
    private void printByDate(List<Transaction> transactionsAccount,Predicate<Transaction> pred){
        var transactionList = transactionsAccount
                                            .stream()
                                            .filter(pred)
                                            .toList();
        if (transactionList.isEmpty()) {
            System.out.println("\t=>> No results ");
        } else {
            transactionList.forEach(System.out::println);
        }
    }

    /**
     *
     * @param transactionMap  object Map<BigInteger,List<Transaction>>
     * @param account object Account to find
     * @return   List of account transactions
     */
    private List<Transaction> getTransactionOfAccount(Map<BigInteger,List<Transaction>> transactionMap,Account account){
        var transactions = transactionMap.get(account.getNumber());
        return transactions != null ? new ArrayList<>(transactions) : new ArrayList<>();
    }
    /**
     *
     * @param transactions List with Transactions
     * @return  object Map<BigInteger,List<Transaction>> , grouping by Account grom transactions
     */
    private Map<BigInteger,List<Transaction>> groupByNumberAccount(List<Transaction> transactions){
        return transactions
                .stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getAccount().getNumber()));
    }

    /**
     *
     * @param transactionsMap object Map<Account,List<Transaction>>
     * @return  List with all transactions from transactionsMap
     */
    private List<Transaction> getAllTransactions(Map<BigInteger,List<Transaction>> transactionsMap){
        return transactionsMap
                .values()
                .stream()
                .flatMap(Collection::stream)
                .toList();
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
