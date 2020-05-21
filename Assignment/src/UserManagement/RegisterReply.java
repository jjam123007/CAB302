package UserManagement;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class RegisterReply implements Serializable {
    private String userName;
    private String password;

    UserPermissions permissions;
    public RegisterReply(RegisterRequest registerRequest) throws NoSuchAlgorithmException {
        this.userName = userName;
        this.password = DataSecurity.hash(password);
        this.permissions = permissions;
        System.out.println(DataSecurity.hash(password));
    }

}
