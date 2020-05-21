package UserManagement;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

    public class RegisterRequest implements Serializable {
        private String username;
        private String password;
        private String sessionToken;
        private UserPermissions permissions;

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getSessionToken() { return sessionToken; }
        public UserPermissions getPermissions() { return permissions; }

        public RegisterRequest(String username, String password, UserPermissions permissions, String sessionToken) throws NoSuchAlgorithmException {
            this.username = username;
            this.password = DataSecurity.hash(password);
            this.sessionToken = sessionToken;
            this.permissions = permissions;
            System.out.println(DataSecurity.hash(password));
        }

}
