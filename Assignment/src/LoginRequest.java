import java.io.Serializable;
import java.security.NoSuchAlgorithmException;


public class LoginRequest implements Serializable {
    String userName;
    String password;

    public LoginRequest(String userName,String password) throws NoSuchAlgorithmException {
        this.userName = userName;
        this.password = Password.Hash(password);
        System.out.println(Password.Hash(password));
    }


}
