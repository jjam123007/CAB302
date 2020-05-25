package UserManagement.Replies;

import User.ServerUserSession;

import java.io.Serializable;
import java.sql.SQLException;

public class LogoutReply extends Reply implements Serializable {

    public LogoutReply(String sessionToken) throws SQLException {
        super(sessionToken);
        ServerUserSession.removeSession(sessionToken);
        if (ServerUserSession.sessionExists(sessionToken) == false) {
            this.success = true;
        } else {
            this.errorMessage = "We could not log you out. Please try again.";
        }
    }
}
