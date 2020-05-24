package UserManagement.Requests;

import User.UserPermissions;
import UserManagement.DataSecurity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

    public class RegisterRequest implements Serializable {
        private String username;
        private String password;
        private UserPermissions permissions;

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public UserPermissions getPermissions() { return permissions; }

        public RegisterRequest(String username, String password, UserPermissions permissions) throws NoSuchAlgorithmException {
            this.username = username;
            this.password = DataSecurity.hash(password);
            this.permissions = permissions;
            System.out.println(DataSecurity.hash(password));
        }

}
