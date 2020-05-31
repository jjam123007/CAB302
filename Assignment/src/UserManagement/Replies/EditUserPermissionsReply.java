package UserManagement.Replies;

import Database.DBConnection;
import Networking.Reply;
import Networking.ReplyError;
import User.PermissionType;
import User.UserPermissions;
import User.ServerUserSession;
import UserManagement.Requests.EditUserPropertyRequest;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Nikolai Taufao | N10481087
 */
public class EditUserPermissionsReply extends Reply {

    /**
     * Change the permissions of a user specified in the request only if the request client user is an admin.
     * @param editUserPropertyRequest object containing the target user and their new permissions.
     * @param sessionToken
     * @throws SQLException
     */
    public EditUserPermissionsReply(EditUserPropertyRequest editUserPropertyRequest, String sessionToken) throws SQLException {
        super(sessionToken);
        if (tokenExpired) return;
        String username = editUserPropertyRequest.getUsername();
        UserPermissions userPermissions = editUserPropertyRequest.getPermissions();

        if (ServerUserSession.hasPermission(sessionToken, PermissionType.editUsers)) {
            checkUserPermissions(username, userPermissions, sessionToken);
        } else {
            this.errorMessage = ReplyError.userNotPermitted;
        }
    }

    /**
     * Check if the target user is same as the user that made the request and is also admin. If this is the case, send back an
     * error message to the client.
     * @param username target user.
     * @param userPermissions new user permissions.
     * @param sessionToken
     */
    private void checkUserPermissions(String username, UserPermissions userPermissions, String sessionToken) {
        String clientUser = ServerUserSession.getUsername(sessionToken);
        boolean targetUserIsClientUser = username.equals(clientUser);

        if (targetUserIsClientUser && userPermissions.canEditUsers() == false){
            this.errorMessage="You cant remove permission 'edit users' from your own account!";
        }else{
            changeUserPermissions(username, userPermissions);
        }

    }

    /**
     * Execute the sql query to change the specified user's permissions.
     * @param username target user.
     * @param userPermissions new user permissions.
     */
    private void changeUserPermissions(String username, UserPermissions userPermissions) {
        try {
            int p1 = userPermissions.canCreateBillboards() ? 1 : 0;
            int p2 = userPermissions.canEditBillboards() ? 1 : 0;
            int p3 = userPermissions.canScheduleBillboards()? 1 : 0;
            int p4 = userPermissions.canEditUsers()? 1 : 0;

            String updatePermissions = "UPDATE permissions SET " +
                    "createBillboards='"+p1+"'," +
                    "editBillboards='"+p2+"'," +
                    "scheduleBillboards='"+p3+"'," +
                    "editUsers='"+p4+"' WHERE username='"+username+"'; ";

            Statement statement = DBConnection.getInstance().createStatement();
            statement.executeQuery(updatePermissions);
            statement.close();
            this.success = true;

        } catch (SQLException exception){
            exception.printStackTrace();
            this.errorMessage = exception.getMessage();
        }
    }

}
