package org.app.customer.transaction;

import java.util.Arrays;

public enum TransactionType {
    DEPOSIT,WITHDRAW;

    /**
     *
     * @param nameEnumInput String as name enum to find
     * @return  true,if enum type about nameEnumRead name not exist, else false
     */
    public static boolean isTypeNotCorrect(String nameEnumInput){
        if(nameEnumInput == null || nameEnumInput.isEmpty()){
            throw new IllegalArgumentException("Invalid nameEnum argument in TransactionType");
        }
        return Arrays.stream(TransactionType.values())
                .noneMatch(enumType -> enumType.name().equals(nameEnumInput));
    }
}
