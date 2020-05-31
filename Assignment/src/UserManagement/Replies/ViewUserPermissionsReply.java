package UserManagement.Replies;

import Database.DBConnection;
import Networking.Reply;
import Networking.ReplyError;
import User.PermissionType;
import User.UserPermissions;
import User.ServerUserSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * @author Nikolai Taufao | N10481087
 */

public class ViewUserPermissionsReply extends Reply {
    private UserPermissions userPermissions;
    public UserPermissions getUserPermissions() { return userPermissions; }

    /**
     * Send the permissions associated with the given username to the request client only if the client user is an admin.
     * @param username
     * @param sessionToken
     * @throws SQLException
     */
    public ViewUserPermissionsReply(String username, String sessionToken) throws SQLException {
        super(sessionToken);
        if (tokenExpired) return;

        if (ServerUserSession.hasPermission(sessionToken, PermissionType.editUsers)){
            retrieveUserPermissions(username);
        }else{
            this.errorMessage = ReplyError.userNotPermitted;
        }
    }

    /**
     * Retrieve the target user's permissions from the database.
     * @param username
     * @throws SQLException
     */
    private void retrieveUserPermissions(String username) throws SQLException {
        String query = "SELECT * FROM permissions WHERE username = '"+username+"';";
        Statement statement = DBConnection.getInstance().createStatement();
        ResultSet userRows = statement.executeQuery(query);
        if (userRows.next()){
            try {
                boolean canCreateBillboards = userRows.getBoolean(2);
                boolean canEditBillboards = userRows.getBoolean(3);
                boolean canScheduleBillboards = userRows.getBoolean(4);
                boolean canEditUsers = userRows.getBoolean(5);
                userPermissions = new UserPermissions(canCreateBillboards,canEditBillboards,canScheduleBillboards,canEditUsers);
                statement.close();
                this.success = true;
            } catch (SQLException exception){
                this.errorMessage = ReplyError.databaseError;
                exception.printStackTrace();
            }
        }
    }
}
