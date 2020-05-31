package UserManagement.Replies;

import Networking.Reply;
import User.ServerUserSession;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * @author Nikolai Taufao | N10481087
 */
public class LogoutReply extends Reply implements Serializable {

    /**
     * Send the client an error message if the requested logout was not successful.
     * @param sessionToken
     * @throws SQLException
     */
    public LogoutReply(String sessionToken) throws SQLException {
        super(sessionToken);
        if (tokenExpired) return;
        ServerUserSession.removeSession(sessionToken);
        if (!ServerUserSession.isValid(sessionToken)) {
            this.success = true;
        } else {
            this.errorMessage = "We could not log you out. Please try again.";
        }
    }
}
