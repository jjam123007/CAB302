package UserManagement.Replies;

import Database.DBConnection;
import Networking.Reply;
import User.PermissionType;
import User.ServerUserSession;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Nikolai Taufao | N10481087
 */
public class RemoveUserReply extends Reply {

    /**
     * Delete the specified user's account from the database only if the request client user is an admin.
     * @param username target username to delete.
     * @param sessionToken
     * @throws SQLException
     */
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


    /**
     * Send an error message back to the client if the user sending the request is trying to remove their own account.
     * @param username
     * @param sessionToken
     */
    private void checkUser(String username, String sessionToken){
        boolean targetUserIsClient = ServerUserSession.getUsername(sessionToken).equals(username);
        if (!targetUserIsClient){
            removeUser(username);
        } else {
            this.errorMessage = "You cannot remove yourself!";
        }
    }

    /**
     * Remove the user from the database.
     * @param username
     */
    private void removeUser(String username) {
        try {
            Statement statement = DBConnection.getInstance().createStatement();
            String removeUserCredentials = "DELETE FROM users WHERE username = '"+username+"';";
            String removeUserPermissions = "DELETE FROM permissions WHERE username = '"+username+"';";
            statement.executeQuery(removeUserCredentials);
            statement.executeQuery(removeUserPermissions);

            this.success = true;
        } catch (SQLException exception) {
            this.errorMessage = "User has already been removed.";
        }

    }


}
