package UserManagement;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class RegisterReply implements Serializable {
    private boolean success = false;
    private String errorMessage = null;

    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }

    public RegisterReply(RegisterRequest registerRequest) throws SQLException {
        String sessionToken = registerRequest.getSessionToken();
        if (UserSession.hasPermission(sessionToken, PermissionType.editUsers))
        {
            registerUser();
            this.success = true;
        } else {
            this.errorMessage = "User does not have permission";
            System.out.println(this.errorMessage);
        }
    }

    private static void registerUser(){

    }

}
