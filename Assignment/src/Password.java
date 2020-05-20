import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public final class Password {

    public static String Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        return(bytesToString(hash));
    }

    public static String GenerateSalt(){
        Random random = new SecureRandom();
        byte[] saltBytes = new byte[32];
        random.nextBytes(saltBytes);
        String salt = bytesToString(saltBytes);
        return salt;
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
