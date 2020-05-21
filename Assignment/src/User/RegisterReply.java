package User;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import
public class RegisterReply implements Serializable {
    private String userName;
    private String password;

    UserPermissions permissions;
    public RegisterReply(String userName, String password, UserPermissions permissions) throws NoSuchAlgorithmException {
        this.userName = userName;
        this.password = DataSecurity.Hash(password);
        this.permissions = permissions;
        System.out.println(DataSecurity.Hash(password));
    }

}
