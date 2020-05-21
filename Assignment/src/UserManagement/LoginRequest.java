package UserManagement;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;


public class LoginRequest implements Serializable {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public LoginRequest(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        this.password = DataSecurity.hash(password);
        System.out.println(DataSecurity.hash(password));
    }


}
