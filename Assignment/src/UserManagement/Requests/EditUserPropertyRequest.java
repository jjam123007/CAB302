package UserManagement.Requests;

import User.UserPermissions;
import UserManagement.DataSecurity;
import UserManagement.Replies.Reply;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class EditUserPropertyRequest implements Serializable {
    private String username;
    private UserPermissions permissions;
    private String password;

    public String getUsername() { return username; }
    public UserPermissions getPermissions() { return permissions; }
    public String getPassword() { return password; }

    public EditUserPropertyRequest(String username, UserPermissions permissions) {
        this.username = username;
        this.permissions = permissions;
    }

    public EditUserPropertyRequest(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        this.password = DataSecurity.hash(password);
    }

}
