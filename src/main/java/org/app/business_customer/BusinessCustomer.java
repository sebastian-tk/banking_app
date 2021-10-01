package org.app.business_customer;

import lombok.Getter;
import lombok.Setter;
import org.app.customer.*;

import java.util.Scanner;
import java.util.Set;

import static org.app.customer.CustomerDataReaderProvider.*;
import static org.app.customer.ValidatorPersonalData.*;


/*
    Drugi typ to klient biznesowy,który oprócz takich samych danych jak klient indywidualny posiada jeszcze nazwę firmy,
    adres firmy, numer NIP oraz numer REGON. Dopilnuj, żeby w aplikacji wszystkie przechowywane numery miały prawidłowy
    format zgodnie z obowiązującymi przepisami.

    Uwaga klient biznesowy może mieć tylko jedno konto przypisane do danego NIP-u.
    Oprócz tego klient biznesowy za każdym razem, kiedy dokonuje wypłatę środków obciążany jest stała taką samą dla
    każdego klienta biznesowego prowizją od wypłaconych środków. Dodatkowo klient biznesowy może w okresie rozliczeniowym,
    który trwa od 1 dnia każdego miesiąca do ostatniego dnia każdego miesiąca dokonać pewną maksymalną stałą dla każdego
    klienta biznesowego ilość wpłat. Po przekroczeniu tej ilości każda kolejna wpłata obciążona jest stałą dla każdego
    klienta biznesowego prowizją. Na podstawie powyższego opisu przygotuj aplikację, która pozwoli na obsługę klientów
    w banku.
 */
@Getter
@Setter
public class BusinessCustomer extends Customer implements ValidatorPersonalData, CustomerDataReaderProvider {
    private String nameCompany;
    private String addressCompany;
    private String nip;
    private String regon;

    public BusinessCustomer(Account accountNumber) {
        super(accountNumber);
        Scanner scanner = new Scanner(System.in);
        this.nameCompany= readDataFromUser(scanner,"name company: ",this::isNameCompanyNotCorrect);
        this.addressCompany = readDataFromUser(scanner,"address company: : ", ValidatorPersonalData::isAddressNotCorrect);
        this.nip = readDataFromUser(scanner,"NIP: ",BusinessCustomer::isNipNotCorrect);
        this.regon = readDataFromUser(scanner,"REGON: ",BusinessCustomer::isRegonNotCorrect);

    }

    private BusinessCustomer(String name, String surname, Pesel pesel, String address, String email, String phoneNumber,
                               Set<Account> accountSet, String nameCompany, String addressCompany, String nip, String regon) {
        super(name, surname, pesel, address, email, phoneNumber, accountSet);
        this.nameCompany = nameCompany;
        this.addressCompany = addressCompany;
        this.nip = nip;
        this.regon = regon;
    }

    /**
     *
     * @param name String as name customer
     * @param surname String as surname customer
     * @param pesel String as number pesel
     * @param address   String as address customer
     * @param email String as email customer
     * @param phoneNumber   String as phone number
     * @param accountSet    Set with Accounts
     * @param nameCompany   String as customer's name company
     * @param addressCompany String as customer's address company
     * @param nip   String as nip company
     * @param regon String as regon company
     * @return  new BusinessCustomer
     */
    public static BusinessCustomer createBusinessCustomer(String name, String surname, String pesel, String address, String email, String phoneNumber,
                                                          Set<Account> accountSet, String nameCompany, String addressCompany, String nip, String regon) {
        Customer.createCustomer(name, surname, pesel, address, email, phoneNumber, accountSet);
        if (nameCompany == null || nameCompany.isEmpty()) {
            throw new IllegalArgumentException("Invalid nameCompany argument when create business customer");
        }
        if (addressCompany == null || addressCompany.isEmpty()) {
            throw new IllegalArgumentException("Invalid address company argument when create business customer");
        }
        if (isAddressNotCorrect(addressCompany)) {
            throw new IllegalArgumentException("Incorrect company address syntax when creating a business customer");
        }
        if (nip == null || nip.isEmpty()) {
            throw new IllegalArgumentException("Invalid nip argument when create business customer");
        }
        if (isNipNotCorrect(nip)) {
            throw new IllegalArgumentException("Invalid nip syntax when create business customer");
        }
        if (regon == null || regon.isEmpty()) {
            throw new IllegalArgumentException("Invalid regon argument when create business customer");
        }
        if (isRegonNotCorrect(regon)) {
            throw new IllegalArgumentException("Invalid regon syntax when create business customer");
        }
        if(accountSet.size() !=1){
            throw new IllegalArgumentException("Invalid accounts for NIP:"+nip +".One account number may be assigned to one NIP");
        }
        return new BusinessCustomer(name,surname,Pesel.createPesel(pesel),address,email,phoneNumber,accountSet,nameCompany,addressCompany,nip,regon);
    }

    /**
     * @param nip String as nip
     * @return true, if nip is not correct, else false
     */
    public static boolean isNipNotCorrect(String nip) {
        int[] weights = { 6, 5, 7, 2, 3, 4, 5, 6, 7};
        return !nip.matches("[0-9]{10}") || isSumNotControlCorrect(nip,weights);
    }
    /**
     * @param regon String as nip
     * @return true, if nip is not correct, else false
     */
    public static boolean isRegonNotCorrect(String regon) {
        int[] weights = {8,9,2,3,4,5,6,7};
        return !regon.matches("[0-9]{9}") || isSumNotControlCorrect(regon,weights) || isProvinceNumberNotCorrect(regon);
    }

    /**
     * @param expression String as expression with numbers
     * @return true, if sum control is not correct, else false
     */
    private static boolean isSumNotControlCorrect(String expression, int[] weights) {
        if ((weights.length + 1) != expression.length()) {
            throw new IllegalArgumentException("Invalid size of arguments");
        }
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += (Character.getNumericValue(expression.charAt(i)) * weights[i]);
        }
        sum = (sum == 10) ? 0 : sum;
        return (sum % 11) != Character.getNumericValue(expression.charAt(expression.length()-1));
    }
    /**
     * @param regon String as nip
     * @return true, if number province is not correct in nip, else false
     */
    private static boolean isProvinceNumberNotCorrect(String regon) {
        int number = Character.getNumericValue(regon.charAt(0)) * 10 + Character.getNumericValue(regon.charAt(1));
        for (int i = 2; i <= 98; i += 2) {
            if (number == i) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param expression String as name company to check
     * @return  true, if expression is not correct, else false
     */
    private boolean isNameCompanyNotCorrect(String expression){
        return !expression.matches("^[a-zA-Z]{2,}$");
    }

}