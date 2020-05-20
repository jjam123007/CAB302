import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Hash {

    public static String getHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        return(toHex(hash));
    }

    //Code stolen from "CAB302 Week 9: Assignment Q&A" video
    private static String toHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : hash) {
            hexString.append(String.format("%02x",b&0xFF));
        }
        return hexString.toString();
    }
}
