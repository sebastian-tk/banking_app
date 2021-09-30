package org.app.customer;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.IntStream;

@EqualsAndHashCode
@ToString
@Getter
public class Pesel{
    private static final int NUMBER_DIGITS_PESEL = 11;
    private static final int[] WEIGHTS = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
    private String number;

    private Pesel(String number) {
        this.number = number;
    }

    public static Pesel createPesel(String number){
        if (number == null || number.isEmpty()) {
            throw new IllegalArgumentException("Invalid number argument");
        }
        if(isNotOnlyDigits(number)){
            throw new IllegalArgumentException("Illegal characters in the PESEL number");
        }
        if( areValuesNotEqual(number.length(), NUMBER_DIGITS_PESEL)){
            throw new IllegalArgumentException("Invalid length of number");
        }
        if (isDateNotExist(getDate(number)) || isSumControlNotCorrect(number)) {
            throw new IllegalArgumentException("Invalid number number");
        }
        return new Pesel(number);
    }

    /**
     *
     * @param pesel String as number to check
     * @return  true, if number is correct, else false
     */
    public static boolean isPeselCorrect(String pesel){
        try{
            new Pesel(pesel);
            return true;
        }catch (IllegalArgumentException exc){
            return false;
        }
    }
    /**
     *
     * @param pesel String as number number
     * @return  String as date from number
     */
    private static String getDate(String pesel){
        return pesel.substring(0,6);
    }
    /**
     * @param firstVal  first integer value to compare
     * @param secondVal second integer value to compare
     * @return true, if values are not equal, else false
     */
    private static boolean areValuesNotEqual(int firstVal, int secondVal) {
        return firstVal != secondVal;
    }

    /**
     * @param expression String as expression to check
     * @return true, if expression has not only digits, else false
     */
    private static boolean isNotOnlyDigits(String expression) {
        return !expression.matches("[0-9]+");
    }

    /**
     * @param date String in format "yyMMdd" as date to check
     * @return true, if date not exist, else false
     */
    private static boolean isDateNotExist(String date) {
        if (date.length() != 6) {
            throw new IllegalArgumentException("Too short date");
        }
        DateFormat format = new SimpleDateFormat("yyMMdd");
        format.setLenient(false);
        try {
            format.parse(date);
        } catch (ParseException exc) {
            return true;
        }
        return false;
    }

    /**
     * @param pesel String as number with eleven digits
     * @return true, if sum control is not equal with last number in number, else false
     */
    private static boolean isSumControlNotCorrect(String pesel) {
        if(pesel.length()!=NUMBER_DIGITS_PESEL){
            return false;
        }
        var peselDigits = pesel.chars().map(Character::getNumericValue).toArray();
        var sum = IntStream.range(0, Math.min(WEIGHTS.length, peselDigits.length))
                .map(i -> (WEIGHTS[i] * peselDigits[i]))
                .map(res -> convertToString(res).length() > 1 ? res % 10 : res)
                .sum();
        int sumControl = (convertToString(sum).length() > 1 ? (sum % 10) : sum);
        return peselDigits[10] != (10 - sumControl) ;
    }

    /**
     * @param value integer value
     * @return String as converted value
     */
    private static String convertToString(int value) {
        return Integer.toString(value);
    }
}
