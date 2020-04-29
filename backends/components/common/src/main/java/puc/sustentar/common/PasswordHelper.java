package puc.sustentar.common;


import com.google.common.io.BaseEncoding;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PasswordHelper {


    final static String[] BAD_PASSWORD_SEQUENCES = {"0123", "1234", "2345", "3456", "4567", "5678", "6789",
            "9876", "8765", "7654", "6543", "5432", "4321", "3210",
            "0000", "1111", "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999"};

    public static final int PASSWORD_LENGTH = 4;


    public static class SaltAndHash {
        public final String salt;
        public final String hash;

        public SaltAndHash(String salt, String hash) {
            this.salt = salt;
            this.hash = hash;
        }
    }

    //TODO shouldn't be static, should be instance and mockable.
    public static boolean passwordDoesntMeetStandards(String password) {

        if (password.length() < PASSWORD_LENGTH) {
            return true;
        }
        for (char c : password.toCharArray()) {
            if (!Character.isDigit(c))
                return true;
        }
        for (String badSequence : BAD_PASSWORD_SEQUENCES) {
            if (password.contains(badSequence)) {
                return true;
            }
        }
        return false;
    }

    public static boolean passwordDoesntMeetsStandardsV2(String password) {
        if (password.length() != PASSWORD_LENGTH) {
            return true;
        }
        for (char c : password.toCharArray()) {
            if (!Character.isDigit(c))
                return true;
        }
        for (String badSequence : BAD_PASSWORD_SEQUENCES) {
            if (password.contains(badSequence)) {
                return true;
            }
        }
        return false;
    }


    public static SaltAndHash makePwHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        return new SaltAndHash(encodeUsingGuava(salt), encodeUsingGuava(hash));
    }

    public static String encodeUsingGuava(byte[] bytes) {
        return BaseEncoding.base16().encode(bytes);
    }

    public static byte[] decodeUsingGuava(String hexString) {
        return BaseEncoding.base16()
                .decode(hexString.toUpperCase());
    }

    public static byte[] generateSalt(SecureRandom sr) throws NoSuchAlgorithmException {
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

}
