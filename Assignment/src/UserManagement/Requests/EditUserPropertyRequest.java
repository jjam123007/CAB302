package UserManagement.Requests;

import User.UserPermissions;
import UserManagement.DataSecurity;
import UserManagement.Replies.Reply;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * @author Nikolai Taufao | N10481087
 */

public class EditUserPropertyRequest implements Serializable {
    private String username;
    private UserPermissions permissions;
    private String password;

    public String getUsername() { return username; }
    public UserPermissions getPermissions() { return permissions; }
    public String getPassword() { return password; }


    /**
     * Send a request to the server that involves editing a target user's permissions.
     * @param username target user.
     * @param permissions new user permissions..
     */
    public EditUserPropertyRequest(String username, UserPermissions permissions) {
        this.username = username;
        this.permissions = permissions;
    }

    /**
     * Send a request to the server that involves editing a target user's password.
     * @param username target user.
     * @param password new user password.
     */
    public EditUserPropertyRequest(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        this.password = DataSecurity.hash(password);
    }

}
