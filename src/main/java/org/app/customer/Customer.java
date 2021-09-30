package org.app.customer;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;

import static org.app.customer.ValidatorPersonalData.*;


/*
    Pierwszy z nich to klient indywidualny, który opisywany jest przez imię, nazwisko, adres zamieszkania,
    adres email, numer telefonu oraz numer PESEL. Klient posiada jedno lub więcej kont bankowych, które opisywane są
    przez nazwę, unikalny numer oraz przez ilość środków, które na nich się znajdują.
 */
@EqualsAndHashCode
@ToString
@Getter
public class Customer implements ValidatorPersonalData{
    private String name;
    private String surname;
    private Pesel pesel;
    private String address;
    private String email;
    private String phoneNumber;
    private Set<Account> accountSet;

    public Customer(Account accountNumber){
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
     *
     * @param name String as name
     * @param surname   String as surname
     * @param pesel String as number
     * @param address   String as address
     * @param email String as email
     * @param phoneNumber   String as phoneNumber
     * @param accountSet    reference HasSet<Account>
     * @return  created new Customer
     */
    public static Customer createCustomer(String name, String surname, String pesel, String address, String email, String phoneNumber, Set<Account> accountSet) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name argument when create customer");
        }
        if(surname == null || surname.isEmpty()){
            throw new IllegalArgumentException("Invalid surname argument when create customer");
        }
        if(address == null || address.isEmpty()){
            throw new IllegalArgumentException("Invalid address argument when create customer");
        }
        if(isAddressNotCorrect(address)){
            throw new IllegalArgumentException("Incorrect address syntax: "+address);
        }
        if(email==null || email.isEmpty()){
            throw new IllegalArgumentException("Invalid email argument when create customer");
        }
        if(isEmailNotCorrect(email)){
            throw new IllegalArgumentException("Incorrect email syntax: "+email);
        }
        if(phoneNumber == null || phoneNumber.isEmpty()){
            throw new IllegalArgumentException("Invalid phoneNumber argument when create customer");
        }
        if(isNumberNotCorrect(phoneNumber)){
            throw new IllegalArgumentException("Incorrect phoneNumber syntax");
        }
        if(accountSet == null || accountSet.isEmpty()){
            throw new IllegalArgumentException("Invalid accountSet argument when create customer");
        }
        return new Customer(name,surname,Pesel.createPesel(pesel),address,email,phoneNumber,accountSet);
    }

    public void service(){

    }



    /**
     *
     * @param scanner object Scanner to read data from user
     *                Method loads subsequent fields of the Customer class except Account
     */
    private void init(Scanner scanner){
        this.name = readDataFromUser(scanner,"name: ", ValidatorPersonalData::isNameOrSurnameNotCorrect);
        this.surname = readDataFromUser(scanner,"surname: ",ValidatorPersonalData::isNameOrSurnameNotCorrect);
        this.pesel = Pesel.createPesel(readDataFromUser(scanner,"pesel: ",dataFromUser->!Pesel.isPeselCorrect(dataFromUser)));
        this.address = readDataFromUser(scanner,"address: ",ValidatorPersonalData::isAddressNotCorrect);
        this.email=readDataFromUser(scanner,"email: ",ValidatorPersonalData::isEmailNotCorrect);
        this.phoneNumber = readDataFromUser(scanner,"phoneNumber: ",ValidatorPersonalData::isNumberNotCorrect);
    }

    /**
     *
     * @param scanner object Scanner
     * @param nameData  String as name of data to read
     * @param pred  interface Predicate
     * @return  correct String from user
     */
    private String readDataFromUser(Scanner scanner, String nameData, Predicate<String> pred){
        String buffer;
        boolean run;
        do{
            buffer = readExpression(scanner,"enter "+nameData);
            run= pred.test(buffer);
            if(run){
                System.out.println("\t#Invalid "+nameData);
            }
        }while (run);
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


}
