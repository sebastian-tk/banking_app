package org.app;

import org.app.bank.Bank;
import org.app.customer.Account;
import org.app.customer.Customer;
import org.app.customer.CustomersService;
import org.app.customer.EncryptedPassword;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.app.bank.Bank.*;
import static org.app.customer.EncryptedPassword.*;

/*
    Napisz program, który pozwoli zaimplementować mechanizm obsługujący klienta w aplikacji bankowej. Bank posiada dwa
    typy klientów.

    Pierwszy z nich to klient indywidualny, który opisywany jest przez imię, nazwisko, adres zamieszkania,
    adres email, numer telefonu oraz numer PESEL. Klient posiada jedno lub więcej kont bankowych, które opisywane są
    przez nazwę, unikalny numer oraz przez ilość środków, które na nich się znajdują.

    Drugi typ to klient biznesowy,który oprócz takich samych danych jak klient indywidualny posiada jeszcze nazwę firmy,
    adres firmy, numer NIP oraz numer REGON. Dopilnuj, żeby w aplikacji wszystkie przechowywane numery miały prawidłowy
    format zgodnie z obowiązującymi przepisami.

    Uwaga klient biznesowy może mieć tylko jedno konto przypisane do danego NIP-u.
    Oprócz tego klient biznesowy za każdym razem, kiedy dokonuje wypłatę środków obciążany jest stała taką samą dla
    każdego klienta biznesowego prowizją od wypłaconych środków. Dodatkowo klient biznesowy może w okresie rozliczeniowym,
    który trwa od 1 dnia każdego miesiąca do ostatniego dnia każdego miesiąca dokonać pewną maksymalną stałą dla każdego
    klienta biznesowego ilość wpłat. Po przekroczeniu tej ilości każda kolejna wpłata obciążona jest stałą dla każdego
    klienta biznesowego prowizją. Na podstawie powyższego opisu przygotuj aplikację, która pozwoli na obsługę klientów
    w banku. Aplikacja powinna umożliwiać w ramach wygodnego menu:

    a. Rejestrację nowego użytkownika – zaproponuj mechanizm szyfrowania hasła
    b. Logowanie do systemu bankowości – zaproponuj jak rozwiążesz problem logowania
    c. Sprawdzenie salda po zalogowaniu
    d. Dokonanie wypłaty po zalogowaniu z kontrolą dostępności środków i naliczaniem prowizji dla klientów biznesowych
    e. Dokonanie wpłaty po zalogowaniu z naliczaniem prowizji dla klientów biznesowych
    f. Możliwość przeglądania i przeszukiwania historii wpłat. Przyjmij wygodne różne kryteria wyszukiwania transakcji.
       Wyniki wyszukiwania z historii można zapisywać do wybranego przez Ciebie formatu zewnętrznego.
 */
public class App {
    public static void main(String[] args) {
        final String path = "src/main/java/org/app/data/";
        final String fileCustomers = "customers.json";
        final String filePasswords = "passwords.json";
        try{
            Bank bank = createBank(path.concat(fileCustomers),path.concat(filePasswords));
            bank.service();
            bank.addCustomer(Customer.createCustomer(
                    "Jerzy",
                    "Lewandowski",
                    "75072796555",
                    "ul. Konopnickiej 6 90-206 Luban",
                    "jurek.lewy@onet.pl",
                    "699-105-354",
                    Set.of(
                            Account.createAccount("personal", new BigInteger("12345678901234567890666666"), new BigDecimal("1550.5")),
                            Account.createAccount("personal", new BigInteger("12345678901234567888666777"), new BigDecimal("123.4"))
                    )), convertToChars("jurek2")
            );
        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
        }
        System.out.println(" ### BANK CLOSED ###");
    }
}









