package puc.sustentar.common;

import java.security.SecureRandom;

public class CoreTokenChecker {

    final static String A2z029 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    final static SecureRandom rnd = new SecureRandom();


    public static String randomToken(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(A2z029.charAt(rnd.nextInt(A2z029.length())));
        return sb.toString();
    }
}