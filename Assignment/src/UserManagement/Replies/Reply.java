package UserManagement.Replies;

import User.ServerUserSession;

import java.io.Serializable;

public class Reply implements Serializable {
    protected boolean success = false;
    protected boolean sessionExpired = false;
    protected String errorMessage = null;

    public boolean isSuccess() {
        return success;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public Reply(String sessionToken){
        if (!ServerUserSession.sessionExists(sessionToken)){
            this.sessionExpired = true;
            this.errorMessage = "Your session token has expired or is invalid. Please log in again.";
        }
    }

}
