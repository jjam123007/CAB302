package UserManagement.Requests;

import Networking.Request;
import UserManagement.DataSecurity;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
/**
 * @author Nikolai Taufao | N10481087
 */
public class LoginRequest extends Request implements Serializable {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }

    /***
     * Send the username and hashed password to the server for processing.
     * @param username
     * @param password
     * @throws NoSuchAlgorithmException
     */
    public LoginRequest(String username, String password) throws NoSuchAlgorithmException, IOException {
        super();
        this.username = username;
        this.password = DataSecurity.hash(password);
        System.out.println("HELLo1");
        System.out.println("HELLo2");
        System.out.println("HELLo3");
        sendRequest(this);
    }

}
