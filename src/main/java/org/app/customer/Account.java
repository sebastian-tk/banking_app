package org.app.customer;


import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@EqualsAndHashCode
@Getter
public class Account {
    public static final int LENGTH_NUMBER = 26;
    private final String name;
    private final BigInteger number;
    private BigDecimal amountMoney;

    private Account(String name, BigInteger number, BigDecimal amountMoney) {
        this.name = name;
        this.number = number;
        this.amountMoney = amountMoney;
    }

    /**
     *
     * @param name String as name
     * @param number    object of BigInteger
     * @param amountMoney object of BigDecimal
     * @return  new created Account with verified input data
     */
    public static Account createAccount(String name, BigInteger number, BigDecimal amountMoney){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Invalid name argument when creating an account");
        }
        if(number==null || number.toString().length()!= LENGTH_NUMBER){
            throw new IllegalArgumentException("Invalid number argument when creating an account");
        }
        if(amountMoney==null || amountMoney.compareTo(BigDecimal.ZERO)<0){
            throw new IllegalArgumentException("Invalid amountMoney argument when creating an account");
        }
        return new Account(name,number,amountMoney);
    }

    @Override
    public String toString() {
        return  "\taccount: " + name + "\n" +
                "\tnumber: " + number + "\n" +
                "\tamountMoney: " + amountMoney+"zl";
    }

    /**
     *
     * @param moneyToAdd object BigDecimal as amount money to get
     *                 Method add moneyToAdd to amount money
     */
    protected void addMoney(BigDecimal moneyToAdd){
        if(moneyToAdd == null || moneyToAdd.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Incorrect addMoney argument");
        }
        amountMoney = amountMoney.add(moneyToAdd);
    }

    /**
     *
     * @param moneyToGet object BigDecimal as amount money to add
     *                   Method subtract moneyToGet from amountMoney
     */
    protected void subtractMoney(BigDecimal moneyToGet){
        if(moneyToGet == null || moneyToGet.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Invalid moneyToGet argument");
        }
        amountMoney=amountMoney.subtract(moneyToGet);
    }

}
