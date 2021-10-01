package org.app.customer;

import org.app.business_customer.BusinessCustomer;

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
     *                 Method add new Customer to database
     */
    public void add(Customer customer,char[] password){
        if(customer == null){
            throw new IllegalArgumentException("Invalid customer argument when add");
        }
        if(areAccountsNotNumbersUnique(customer.getAccountSet()) || isUserExist(customer.getPesel())){
            throw new IllegalArgumentException("Incorrect data of the user with the pesel number:" + customer.getPesel().getNumber()+". Data exist");
        }
        byte[] salt = generateSalt();
        mapCustomers.put(customer.getPesel(), customer);
        mapHashPasswords
                .put(customer.getPesel(),createEncryptedPassword(customer.getPesel(), mergeHash(salt,generatePasswordHash(password,salt))));
        clearPassword(password);
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
            serveCustomer.service();
        }else{
            System.out.println("\t### ACCESS DENIED ###");
        }
    }

    /**
     *
     * @return  object Customer if successfully login, else null
     */
    private Customer login(){
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
}
