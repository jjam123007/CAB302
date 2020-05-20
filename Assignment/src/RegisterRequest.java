import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

    public class RegisterRequest implements Serializable {
        String userName;
        String password;
        UserPermissions permissions;
        public RegisterRequest(String userName,String password, UserPermissions permissions) throws NoSuchAlgorithmException {
            this.userName = userName;
            this.password = Password.Hash(password);
            this.permissions = permissions;
            System.out.println(Password.Hash(password));
        }

}
