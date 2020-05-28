package User;

import UserManagement.DataSecurity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
/**
 * @author Nikolai Taufao | N10481087
 */
public class LoginRequest implements Serializable {
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
    public LoginRequest(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        this.password = DataSecurity.hash(password);
    }


}
