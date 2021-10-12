package org.app.customer;

import java.util.List;
import java.util.Scanner;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public interface CustomerDataReaderProvider {

    /**
     * @param scanner  object Scanner
     * @param nameData String as name of data to read
     * @param pred     interface Predicate
     * @return correct String from user
     */
    static String readDataFromUser(Scanner scanner, String nameData, Predicate<String> pred) {
        if(scanner == null){
            throw new IllegalArgumentException("Scanner cannot be null in readDataFromUser");
        }
        if(nameData == null || nameData.isEmpty()){
            throw new IllegalArgumentException("Invalid nameData argument in readDataFromUser");
        }
        if(pred == null){
            throw new IllegalArgumentException("Interface Predicate can not be null in readDataFromUser");
        }
        String buffer;
        boolean run;
        do {
            buffer = readExpression(scanner, "enter " + nameData);
            run = pred.test(buffer);
            if (run) {
                System.out.println("\t#Invalid " + nameData);
            }
        } while (run);
        return buffer;
    }

    /**
     * @param scanIn  object Scanner
     * @param message String as message to user
     * @return String as expression from user
     */
    static String readExpression(Scanner scanIn, String message) {
        if(scanIn == null){
            throw new IllegalArgumentException("Scanner cannot be null in readExpression");
        }
        if(message == null || message.isEmpty()){
            throw new IllegalArgumentException("Invalid message argument in readExpression");
        }
        System.out.print(message);
        return scanIn.nextLine();
    }
    /**
     * @param scanner object Scanner
     * @return String as positive double value
     */
    static String readAmountMoney(Scanner scanner) {
        if(scanner == null){
            throw new IllegalArgumentException("Scanner cannot be null in readAmountMoney");
        }
        return readDataFromUser(scanner, "amount money: ", CustomerDataReaderProvider::isDecimalNotPositiveValue);
    }

    /**
     * @param question String as question for user
     * @return true, if user answered yes or false if user answered false
     */
    static boolean isYesOrNo(Scanner scanner,String question) {
        if(scanner == null){
            throw new IllegalArgumentException("Scanner cannot be null in isYesOrNo");
        }
        if(question == null || question.isEmpty()){
            throw new IllegalArgumentException("Invalid question argument in isYesOrNo");
        }
        String buffer;
        do {
            buffer = readExpression(scanner, question + "(yes/no):");
            if (!areEquals(buffer, "yes", String::equals) && !areEquals(buffer, "no", String::equals)) {
                System.out.println("\t# Unavailable answer");
                buffer = "";
            }
        } while (buffer.isEmpty());
        return areEquals(buffer, "yes", String::equals);
    }

    /**
     * @param scanner           object Scanner
     * @param amountOptions integer value as amount options to choice
     * @return integer value as an acceptable number selected by the user
     */
    static  int readChoice(Scanner scanner, int amountOptions) {
        if(scanner == null){
            throw new IllegalArgumentException("Scanner cannot be null in readChoice");
        }
        if(amountOptions<1){
            throw new IllegalArgumentException("Invalid amountOptions argument.Must be positive");
        }
        List<Integer> acceptableChoices = generateNumber(1,amountOptions);
        int bufferChoice;
        do {
            System.out.print("choice: ");
            while (!scanner.hasNextInt()) {
                System.out.println("\t#incorrect value");
                scanner.nextLine();
                System.out.print("choice: ");
            }
            bufferChoice = parseToInt(scanner.nextLine());
            if (!acceptableChoices.contains(bufferChoice)) {
                System.out.println("\tThere is no such choice");
                bufferChoice = 0;
            }
        } while (bufferChoice == 0);
        return bufferChoice;
    }

    /**
     * @param value String as value to parse
     * @return integer value as parsed integer from value
     */
    private static int parseToInt(String value) {
        return Integer.parseInt(value);
    }

    /**
     *
     * @param expression String as expression to check
     * @return  true, if expression does not contain  a parsable positive double, else false
     */
    private static boolean isDecimalNotPositiveValue(String expression){
        try{
            double val=Double.parseDouble(expression);
            return val < 0;
        }catch (Exception e){
            return true;
        }
    }
    /**
     * @param firstObj  second object to compare
     * @param secondObj first object to compare
     * @return true, if firstObj is equal with secondObj, else false
     */
    private static  <T> boolean areEquals(T firstObj, T secondObj, BiPredicate<T, T> pred) {
        return pred.test(firstObj, secondObj);
    }

    /**
     * @param minRange minimum range
     * @param maxRange maximum range
     * @return List<Integer> with numbers between minRange and maxRange
     */
    private static List<Integer> generateNumber(int minRange, int maxRange) {
        if (minRange > maxRange) {
            throw new IllegalArgumentException("Invalid range arguments");
        }
        return IntStream.rangeClosed(minRange, maxRange).boxed().toList();
    }
}
