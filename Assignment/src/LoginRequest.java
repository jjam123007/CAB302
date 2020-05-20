import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class LoginRequest {
    String userName;
    String password;

    public LoginRequest(String userName,String password) throws NoSuchAlgorithmException {
        this.userName = userName;
        this.password = Hash.getHash(password);
        System.out.println(Hash.getHash(password));
    }


}
