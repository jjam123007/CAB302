package UserManagement.Replies;

import User.ServerUserSession;

import java.io.Serializable;

/**
 * @author Nikolai Taufao | N10481087
 */
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


    /**
     * Notify the user that their session token has expired or is invalid if it is not in the server.
     * @param sessionToken
     */
    public Reply(String sessionToken){
        if (!ServerUserSession.sessionExists(sessionToken)){
            this.sessionExpired = true;
            this.errorMessage = "Your session token has expired or is invalid. Please log in again.";
        }
    }

}
