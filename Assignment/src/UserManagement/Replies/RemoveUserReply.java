package UserManagement.Replies;

import Database.DBConnection;
import User.PermissionType;
import User.ServerUserSession;

import java.sql.SQLException;
import java.sql.Statement;

public class RemoveUserReply extends Reply {

    public RemoveUserReply(String username, String sessionToken) throws SQLException {
        super(sessionToken);
        if (!sessionExpired) {
            if (ServerUserSession.hasPermission(sessionToken, PermissionType.editUsers)){
                checkUser(username, sessionToken);
            } else {
                this.errorMessage = ReplyError.userNotPermitted;
            }
        }

    }

    private void checkUser(String username, String sessionToken){
        if (!ServerUserSession.getUsername(sessionToken).equals(username)){
            removeUser(username);
        } else {
            this.errorMessage = "You cannot remove yourself!";
        }
    }

    private void removeUser(String username) {
        try {
            Statement statement = DBConnection.getInstance().createStatement();
            String removeUserCredentials = "DELETE FROM user WHERE username = '"+username+"';";
            String removeUserPermissions = "DELETE FROM permissions WHERE username = '"+username+"';";
            statement.executeQuery(removeUserCredentials);
            statement.executeQuery(removeUserPermissions);

            this.success = true;
        } catch (SQLException exception) {
            this.errorMessage = "User has already been removed.";
        }

    }


}
