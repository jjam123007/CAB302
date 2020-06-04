package UserManagement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
/**
 * @author Nikolai Taufao | N10481087
 */
public final class DataSecurity {

    /**
     * Return the SHA-256 hash of the input string.
     * @param input the string to be hashed.
     * @return the SHA-256 hash of the input string.
     * @throws NoSuchAlgorithmException
     */
    public static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        return(bytesToString(hash));

    }

    /**
     * Return a random string of length 32.
     * #Code stolen from "CAB302 Week 9: Assignment Q&A" video
     * @return random string of 32 characters.
     */
    public static String randomString(){
        Random random = new SecureRandom();
        byte[] randomBytes = new byte[32];
        random.nextBytes(randomBytes);
        String randomString = bytesToString(randomBytes);
        return randomString;
    }

    /**
     * Convert a byte array to a string and return it.
     * #Code stolen from "CAB302 Week 9: Assignment Q&A" video
     * @param byteArray
     * @return a string.
     */
    private static String bytesToString(byte[] byteArray) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : byteArray) {
            hexString.append(String.format("%02x",b&0xFF));
        }
        return hexString.toString();
    }
}
