package org.app.customer;

import org.app.business_customer.BusinessCustomer;
import org.app.customer.transaction.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


import static org.app.customer.CustomerDataReaderProvider.*;
import static org.app.customer.EncryptedPassword.*;
import static org.app.customer.Pesel.*;

public class CustomersService {
    private final Scanner scanner;
    private Map<Pesel,EncryptedPassword> mapHashPasswords;
    private Map<Pesel,Customer> mapCustomers;
    private Map<Pesel, List<Transaction>> mapTransactions;
    private Customer serveCustomer;
    private Account serveAccount;


    private CustomersService(Map<Pesel,EncryptedPassword> mapHashPasswords, Map<Pesel,Customer>customers,Map<Pesel,List<Transaction>> mapTransactions) {
        this.mapHashPasswords = mapHashPasswords;
        this.mapCustomers = customers;
        this.mapTransactions = mapTransactions;
        this.serveCustomer = null;
        this.serveAccount = null;
        this.scanner = new Scanner(System.in);
    }

    /**
     * @param mapHashPasswords object Map<Pesel,EncryptedPassword>
     * @param customers     object Map<Pesel,Customer>
     * @return new object serviceCustomer
     */
    public static CustomersService createCustomersService(Map<Pesel,EncryptedPassword> mapHashPasswords, Map<Pesel,Customer> customers,Map<Pesel,List<Transaction>> transactionsHistory) {
        if (mapHashPasswords == null) {
            throw new IllegalArgumentException("Invalid mapHashPasswords argument");
        }
        if (customers == null ) {
            throw new IllegalArgumentException("Invalid customers argument");
        }
        if (transactionsHistory == null ) {
            throw new IllegalArgumentException("Invalid map transactions argument");
        }
        return new CustomersService(mapHashPasswords, customers,transactionsHistory);
    }

    public Map<Pesel, EncryptedPassword> getMapHashPasswords() {
        return mapHashPasswords;
    }

    public Map<Pesel,Customer> getMapCustomers() {
        return mapCustomers;
    }

    public Map<Pesel, List<Transaction>> getMapTransactions() {return mapTransactions;}

    /**
     * The method shows the main menu. Allows choosing login or customer registration
     */
    public void service(){
        menuService();
        switch (readChoice(scanner,2)) {
            case 1 -> registrationNewUser();
            case 2 -> serviceAccounts();
            default -> throw new IllegalStateException("\t#Error-unacceptable choice in service of CustomerService");
        }
    }

    /**
     *
     * @param customer object Customer to add
     * @param password array chars as password
     *                  Method adds new customer , when customer exist,pesel already exist or number account
     *                  already exist throws IllegalArgumentException
     *
     */
    public void add(Customer customer,StringBuilder password){
        if(customer == null){
            throw new IllegalArgumentException("Invalid customer argument when add");
        }
        if(password == null || password.isEmpty()){
            throw new IllegalArgumentException("Invalid password argument when add");
        }
        if(areAccountsNotNumbersUnique(customer.getAccountSet()) && !isUserExist(customer.getPesel())){
            throw new IllegalArgumentException("Incorrect number account: " + customer.getAccountSet()+ ". This account already exist");
        }
        if(isUserExist(customer.getPesel()) && !areAccountsNotNumbersUnique(customer.getAccountSet())){
            throw new IllegalArgumentException("Incorrect pesel customer : " + customer.getPesel().getNumber()+ ". This pesel already exist");
        }
        if(areAccountsNotNumbersUnique(customer.getAccountSet()) && isUserExist(customer.getPesel())){
            throw new IllegalArgumentException("Incorrect customer:" + customer.getName()+" " + customer.getSurname() +". Customer already exist");
        }
            byte[] salt = generateSalt();
            char[] charsPassword = convertToChars(password.toString());
            mapCustomers.put(customer.getPesel(), customer);
            mapHashPasswords
                    .put(customer.getPesel(),createEncryptedPassword(customer.getPesel(), mergeHash(salt,generatePasswordHash(charsPassword,salt))));
            clearPassword(charsPassword);
    }

    /**
     * The method supports account creation by selecting the appropriate account and user
     */
    private void registrationNewUser(){
        menuRegistration();
        int choice = readChoice(scanner,2);
        switch(choice){
            case 1-> {
                serveAccount = Account.createAccount("Personal",getNewAccountNumber(),new BigDecimal("0"));
                serveCustomer = new Customer(serveAccount);
            }
            case 2-> {
                serveAccount = Account.createAccount("Company",getNewAccountNumber(),new BigDecimal("0"));
                serveCustomer = new BusinessCustomer(serveAccount);
            }
            default -> throw new IllegalStateException("\t#Error-unacceptable choice in registration new user of CustomerService");
        }
        String hashPassword = generateFullHash(convertToChars(readExpression(scanner,"enter password:")));
        if(isUserExist(serveCustomer.getPesel())){
            System.out.println("\t#User exist");
        }else{
            mapCustomers.put(serveCustomer.getPesel(), serveCustomer);
            mapHashPasswords.put(serveCustomer.getPesel(), createEncryptedPassword(serveCustomer.getPesel(),hashPassword));
            System.out.println("\t# Successful customer addition");

        }
    }

    /**
     * The method performs the process of logging in and servicing the account. It calls the login () method,
     *          if it is successful, it calls the service for a given customer
     */
    private void serviceAccounts(){
        serveCustomer = login();
        if(serveCustomer != null){
            var updatedTransactions =serveCustomer.service(findTransactionsCustomer());
            mapTransactions.put(serveCustomer.getPesel(), updatedTransactions);
        }else{
            System.out.println("\t### ACCESS DENIED ###");
        }
    }

    /**
     *
     * @return  object Customer if successfully login, else null
     */
    private Customer login(){
        if(isThereNoCustomer()){
            System.out.println("\t => There is no customer");
            return null;
        }
        final int NUMBER_LOGIN_ATTEMPTS = 3;
        Customer customer = null;
        boolean run=true;
        int counter=0;
        String bufferPesel;
        char[] bufferPassword;
        do{
            System.out.println(" ---------- LOGIN USER ----------");
            bufferPesel = readExpression(scanner,"enter pesel: ");
            if(isPeselCorrect(bufferPesel) && isUserExist(createPesel(bufferPesel))){
                counter=0;
                do{
                    bufferPassword = convertToChars(readExpression(scanner,"enter password: "));
                    if(isPasswordCorrect(bufferPassword,bufferPesel)){
                        System.out.println("\t Successful login  ;-)\n");
                        customer = findCustomer(bufferPesel);
                    }else{
                        counter++;
                        System.out.println("\t# Invalid password");
                        run = (counter != NUMBER_LOGIN_ATTEMPTS);
                    }
                }while (run && customer==null);
            }else {
                counter++;
                System.out.println("\t# Invalid number #");
                run = (counter != NUMBER_LOGIN_ATTEMPTS);
            }
        }while (run && customer==null);
        return customer;
    }

    /**
     *
     * @param pesel String as number
     * @return  object Customer assigned to the number number
     */
    private Customer findCustomer(String pesel){
        return isUserExist(Pesel.createPesel(pesel)) ? mapCustomers.get(Pesel.createPesel(pesel)) : null;
    }
    /**
     *
     * @param pesel object Pesel to find
     * @return  true, if user with this number is in  mapCustomers, else false
     */
    private boolean isUserExist(Pesel pesel){
        return  mapHashPasswords.containsKey(pesel) ;
    }

    /**
     *
     * @return Lits with Customers from mapCustomers
     */
    private List<Customer> getCustomersList(){
        return mapCustomers.values().stream().toList();
    }
    /**
     *
     * @return  BigInteger as new unique  number account among customers
     */
    private BigInteger getNewAccountNumber(){
        List<BigInteger> listNumbersAccount = getListNumbersAccount(getCustomersList());
        BigInteger numberRandom;
        do{
            numberRandom = createNewNumber();
        }while (listNumbersAccount.contains(numberRandom));
        return numberRandom;
    }

    /**
     *
     * @return List with Transactions of serve customer
     */
    private List<Transaction> findTransactionsCustomer(){
        var list =mapTransactions.get(serveCustomer.getPesel());
        return   list == null ? new ArrayList<>() : list;
    }

    /**
     *
     * @return  new object BigInteger with random account number about length equals with static
     *          variable LENGTH_NUMBER from Account class
     */
    private static BigInteger createNewNumber(){
        String number = new Random()
                .ints(Account.LENGTH_NUMBER,0,10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
        return new BigInteger(number);
    }
    /**
     *
     * @param customers List<Customer>
     * @return List with BigIntegers from customers map
     */
    private static List<BigInteger> getListNumbersAccount(List<Customer> customers){
        return customers
                .stream()
                .map(customer -> customer.getAccountSet().stream().toList())
                .flatMap(Collection::stream)
                .map(Account::getNumber)
                .toList();
    }
    /**
     * Method prints menu to registration
     */
    private void menuRegistration(){
        System.out.println("\t ****** REGISTRATION *******");
        System.out.println("1. Personal account");
        System.out.println("2. Company account");
    }

    /**
     * Method prints menu to main service
     */
    private void menuService(){
        System.out.println("\t ****** MENU *******");
        System.out.println("1. Registration");
        System.out.println("2. Login");
    }

    /**
     *
     * @param password array chars as password
     * @param pesel String as number
     * @return  true, if password after has is equal with hash from file
     */
    private boolean isPasswordCorrect(char[] password,String pesel){
        String hashPassword = generatePasswordHash(password, getSaltFromHash(pesel));
        String hashBase = getPasswordHash(pesel);
        return hashPassword.equals(hashBase);
    }

    /**
     *
     * @param pesel String as number
     * @return  String as hash password
     */
    private String getPasswordHash(String pesel) {
        return mapHashPasswords
                .get(Pesel.createPesel(pesel))
                .getHashPassword();
    }
    /**
     *
     * @param pesel String as number
     * @return  array bytes as salt
     */
    private byte[] getSaltFromHash(String pesel) {
        return mapHashPasswords
                .get(Pesel.createPesel(pesel))
                .getSalt();
    }
    /**
     *
     * @param accountsSet Set with Accounts
     * @return  true,if all numbers are not unique, else false
     */
    private boolean areAccountsNotNumbersUnique(Set<Account> accountsSet){
        return accountsSet
                .stream()
                .anyMatch(account -> isNumberAccountExist(account.getNumber()));
    }

    /**
     *
     * @param number object BigInteger
     * @return  true, if number account exist, else false
     */
    private boolean isNumberAccountExist(BigInteger number){
        return   mapCustomers
                .values()
                .stream()
                .map(Customer::getAccountSet)
                .toList()
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(account -> account.getNumber().equals(number));
    }

    /**
     *
     * @param expression String as expression to convert
     * @return  array chars from expression
     */
    private char[] convertToChars(String expression){
        return expression.toCharArray();
    }

    /**
     *
     * @return true, if there is not any customer, else false
     */
    private boolean isThereNoCustomer(){
        return mapCustomers.isEmpty();
    }
}
