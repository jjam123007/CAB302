package UserManagement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public final class DataSecurity {

    public static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        return(bytesToString(hash));
    }

    //Code stolen from "CAB302 Week 9: Assignment Q&A" video
    public static String randomString(){
        Random random = new SecureRandom();
        byte[] randomBytes = new byte[32];
        random.nextBytes(randomBytes);
        String randomString = bytesToString(randomBytes);
        return randomString;
    }

    //Code stolen from "CAB302 Week 9: Assignment Q&A" video
    private static String bytesToString(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : hash) {
            hexString.append(String.format("%02x",b&0xFF));
        }
        return hexString.toString();
    }
}
