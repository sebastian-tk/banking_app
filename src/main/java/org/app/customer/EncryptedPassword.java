package org.app.customer;


import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

@EqualsAndHashCode
@ToString
public class EncryptedPassword {
    public final static int BYTES_SALT_LENGTH =16;
    public final static String SEPARATOR_HASH =":";

    private final static int ITERATIONS=1500;
    private final static int KEY_LENGTH=128;
    private final static String NAME_ALGORITHM = "PBKDF2WithHmacSHA1";
    private Pesel pesel;
    private String password;

    private EncryptedPassword(Pesel pesel, String password) {
        this.pesel = pesel;
        this.password = password;
    }

    /**
     *
     * @param pesel String as pesel
     * @param password String as password which consist of: salt:hashPassword
     * @return  new object Encrypted Password
     */
    public static EncryptedPassword createEncryptedPassword(Pesel pesel, String password){
        if(pesel == null){
            throw new IllegalArgumentException("Invalid pesel argument when create encrypt password");
        }
        if(password == null || isPasswordHashNotCorrect(password)){
            throw new IllegalArgumentException("Invalid password argument when create encrypt password");
        }
        return new EncryptedPassword(pesel,password);
    }

    public Pesel getPesel(){
        return pesel;
    }

    protected String getSalt(){
        return divideExpression(password,SEPARATOR_HASH)[0];
    }

    protected String getHashPassword(){
        return divideExpression(password,SEPARATOR_HASH)[1];
    }


    /**
     *
     * @param password array char as password
     * @param salt  array bytes
     * @return  String as hash password
     */
    protected static String generatePasswordHash(char[] password, byte[] salt) {
        PBEKeySpec spec = null;
        char[] charsPassword = null;
        byte[] hashPassword;
        try {
            charsPassword = password;
            spec = new PBEKeySpec(charsPassword, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(NAME_ALGORITHM);
            hashPassword = keyFactory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exc) {
            throw new IllegalStateException(exc.getMessage());
        } finally {
            clearPassword(charsPassword);
            if (spec != null) {
                spec.clearPassword();
            }
        }
        return bytesToHex(hashPassword);
    }

    /**
     *
     * @param password array chars
     *                 Method sets all chars to Character.MIN_VALUE
     */
    protected static void clearPassword(char[] password){
        Arrays.fill(password,Character.MIN_VALUE);
    }

    /**
     *
     * @param password String as hash password
     * @return  true, if password is not correct, else flase
     */
    protected static boolean isPasswordHashNotCorrect(String password){
        if(password.matches("^\\w:\\w$")){
            int lengthSalt = BYTES_SALT_LENGTH * 2;
            String[] expressions =divideExpression(password,SEPARATOR_HASH);
            return expressions[0].length()!= lengthSalt || expressions[1].length() <lengthSalt;
        }
        return true;
    }

    /**
     *
     * @param expression String as expression to convert
     * @return  array chars from expression
     */
    protected static char[] convertToChars(String expression){
        return expression.toCharArray();
    }

    /**
     *
     * @param bytes array bytes
     * @return  String as formatted bytes hexadecimal  by pattern: width 2 , the result will be zero-padded
     */
    protected static String bytesToHex(byte[] bytes){
        StringBuilder strBui = new StringBuilder();
        for(var oneByte : bytes){
            strBui.append(String.format("%02X",oneByte));
        }
        return strBui.toString();
    }

    /**
     *
     * @param expression String as expression to convert
     * @return  bytes array from expression
     */
    protected static byte[] stringToBytes(String expression){
        if (expression == null || expression.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression argument when convert to bytes");
        }
        if (expression.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even number of characters.");
        }
        byte[] bytes = new byte[expression.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            bytes[i] = (byte) Integer.parseInt(expression.substring(index, index + 2), 16);
        }
        return bytes;
    }

    /**
     *
     * @param expression String as expression to split
     * @param separator String as separator
     * @return array Strings from expression by separator
     */
    private static String[] divideExpression(String expression,String separator){
        return expression.split(separator);
    }


}
