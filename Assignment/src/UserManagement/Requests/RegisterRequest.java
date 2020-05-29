package UserManagement.Requests;

import Networking.Request;
import User.UserPermissions;
import UserManagement.DataSecurity;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
/**
 * @author Nikolai Taufao | N10481087
 */
    public class RegisterRequest extends Request implements Serializable {
        private String username;
        private String password;
        private UserPermissions permissions;

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public UserPermissions getPermissions() { return permissions; }


    /**
     * Send a request to the server to register a user's username, password and permissions to the database.
     * @param username new user's username.
     * @param password new user's password.
     * @param permissions new user's permissions.
     * @throws NoSuchAlgorithmException
     */
        public RegisterRequest(String username, String password, UserPermissions permissions) throws NoSuchAlgorithmException, IOException {
            super();
            this.username = username;
            this.password = DataSecurity.hash(password);
            this.permissions = permissions;
            System.out.println(DataSecurity.hash(password));
            sendRequest(this);
        }

}
