package org.app.customer;

 public interface ValidatorPersonalData {
    /**
     *
     * @param address String as address to check
     * @return  true, if address is not correct, else false
     *          example pattern "ul. Adama Mickiewicza 10/23 10-100 Warszawa"
     */
     static boolean isAddressNotCorrect(String address){
        return !address.matches("(ul\\.( [A-Z][a-z]+)+) ([0-9]\\d{0,2}(/[0-9]\\d{0,2})?) ([0-9]{2}-[0-9]{3}) ([A-Z][a-z]+)");
    }

    /**
     *
     * @param email String as email
     * @return  true, if email is not correct, else false
     *          pattern: firstpart.secondpart@gmail.com/outlook.com/wp.pl/onet.pl
     */
     static boolean isEmailNotCorrect(String email){
        return !email.matches("[a-z]{1,20}\\.[a-z]{1,20}@((((gmail)|(outlook))\\.com)|(((onet)|(wp))\\.pl))");
    }

    /**
     *
     * @param number String as number
     * @return  true if the number is not correct, otherwise false if the pattern of the number consists of 9 digits
     *          separated by '-' three digits each, for example: 123-456-789
     */
     static boolean isNumberNotCorrect(String number){
        return !number.matches("(\\d{3}-){2}\\d{3}");
    }

    /**
     *
     * @param expression String as name or surname to check
     * @return  true, if expression is not correct, else false if expression consists of at least two letters, 
     *          the first of which is capital
     */
     static boolean isNameOrSurnameNotCorrect(String expression){
        return !expression.matches("^[A-Z][a-z]{2,}$");
    }
}
