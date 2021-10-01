package org.app.customer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        switch (readChoice(generateNumber(1, 1))) {
            case 1 -> registrationNewUser();
            case 2 -> serviceLogin();
            default -> throw new IllegalStateException("\t#Error-unacceptable choice in service of CustomerService");
        }
    }
    /**
     * The method supports account creation by selecting the appropriate account and user
     */
    private void registrationNewUser(){
        menuRegistration();
        int choice =readChoice(generateNumber(1,1));
        switch(choice){
            case 1-> {
                serveAccount = Account.createAccount("Personal",getNewAccountNumber(),new BigDecimal("0"));
                serveCustomer = new Customer(serveAccount);
            }
        }
        String hashPassword = generateFullHash(convertToChars(readExpression("enter password:")));
        if(isUserExist(serveCustomer.getPesel())){
            System.out.println("\t#User exist");
        }else{
            mapCustomers.put(serveCustomer.getPesel(), serveCustomer);
            mapHashPasswords.put(serveCustomer.getPesel(), createEncryptedPassword(serveCustomer.getPesel(),hashPassword));
            System.out.println("\t# Successful customer addition");

        }
    }

    /**
     * The method takes care of the account. The method logs the user in and then allows:
     *
     */
    private void serviceLogin(){
        String peselBuffer = readDataFromUser( "pesel: ", dataFromUser -> !isPeselCorrect(dataFromUser));
        serveCustomer = findCustomer(peselBuffer);
        if(serveCustomer != null){
            Pesel peselRead = createPesel(peselBuffer);
            serveCustomer.service(mapHashPasswords.get(peselRead).getHashPassword(),mapHashPasswords.get(peselRead).getSalt());
        }else{
            System.out.println("\t#Customer does not exist ");
        }
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
     * @param acceptableChoices List<Integer>
     * @return integer value as an acceptable number selected by the user
     */
    private int readChoice(List<Integer> acceptableChoices){
        int bufferChoice;
        do{
            System.out.print("choice: ");
            while (!scanner.hasNextInt()){
                System.out.println("\t#incorrect value");
                scanner.nextLine();
            }
            bufferChoice = parseToInt(scanner.nextLine());
            if(!acceptableChoices.contains(bufferChoice)){
                System.out.println("\tThere is no such choice");
                bufferChoice=0;
            }
        }while (bufferChoice==0);
        return bufferChoice;
    }

    /**
     * @param message String as message to user
     * @return String as expression from user
     */
    private String readExpression(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     *
     * @param minRange minimum range
     * @param maxRange maximum range
     * @return  List<Integer> with numbers between minRange and maxRange
     */
    private List<Integer> generateNumber(int minRange, int maxRange){
        if(minRange>maxRange){
            throw new IllegalArgumentException("Invalid range arguments");
        }
        return IntStream.rangeClosed(minRange, maxRange).boxed().toList();
    }
    /**
     *
     * @param value String as value to parse
     * @return  integer value as parsed integer from value
     */
    private int parseToInt(String value){
        return Integer.parseInt(value);
    }
    /**
     *
     * @param nameData String as name of data to read
     * @param pred     interface Predicate
     * @return correct String from user
     */
    private String readDataFromUser( String nameData, Predicate<String> pred) {
        String buffer;
        boolean run;
        do {
            buffer = readExpression("enter " + nameData);
            run = pred.test(buffer);
            if (run) {
                System.out.println("\t#Invalid " + nameData);
            }
        } while (run);
        return buffer;
    }

}
