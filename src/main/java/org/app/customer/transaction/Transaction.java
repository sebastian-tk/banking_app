package org.app.customer.transaction;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.app.customer.Account;
import org.app.customer.Pesel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;


@Getter
@EqualsAndHashCode
public class Transaction {
    private Pesel pesel;
    private TransactionType type;
    private BigDecimal money;
    private String date;
    private Account account;

    private Transaction(Pesel pesel,TransactionType type,BigDecimal money,String date,Account account) {
        this.pesel = pesel;
        this.type = type;
        this.money = money;
        this.date = date;
        this.account = account;
    }

    /**
     *
     * @param pesel object Pesel
     * @param type  object TransactionType
     * @param money object BigDecimal
     * @param date String as date
     * @param account   object Account
     * @return  new object Transaction
     */
    public static Transaction createTransaction(Pesel pesel,TransactionType type,BigDecimal money,String date,Account account){
        checkReference(pesel,"pesel");
        checkReference(type,"type transaction");
        checkReference(money,"money");
        checkReference(date,"date");
        checkReference(account,"account");

        if(money.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Invalid money argument.Should be positive value");
        }
        if(isDateNotCorrect(date)){
            throw new IllegalArgumentException("Invalid date argument.Incorrect syntax");
        }
        return new Transaction(pesel, type, money, date, account);
    }

    /**
     *
     * @param date String as date by pattern yyyy-MM-dd
     * @return  true, if date is not correct, else false
     */
    public static boolean isDateNotCorrect(String date){
        try{
            parseDate(date);
            return false;
        }catch (DateTimeParseException exc){
            return true;
        }
    }
    /**
     *
     * @param date String as date by pattern yyyy-MM-dd
     * @return  true, if date is not correct, else false
     */
    public static LocalDate parseDate(String date){
           return LocalDate
                    .parse(date, DateTimeFormatter.ofPattern("uuuu-MM-dd")
                            .withResolverStyle(ResolverStyle.STRICT));
    }

    @Override
    public String toString() {
        return  "transaction:" + type + "\n" +
                " money: " + money + "zl\n"+
                " date: " + date + "\n"+
                " " + account+"\n";
    }

    /**
     *
     * @param objectInput object Object
     * @param argument   String as message to print
     *                  The method checks if the input object is null and returns an IllegalArgumentException
     */
    private static void checkReference(Object objectInput,String argument){
        if(objectInput ==null){
            throw new IllegalArgumentException("Invalid "+argument+" argument when create Transaction");
        }
    }
}
