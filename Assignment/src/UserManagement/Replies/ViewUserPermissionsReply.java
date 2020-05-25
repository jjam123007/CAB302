package UserManagement.Replies;

import Database.DBConnection;
import User.PermissionType;
import User.UserPermissions;
import User.UserSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewUserPermissionsReply extends Reply{
    private UserPermissions userPermissions;
    public UserPermissions getUserPermissions() { return userPermissions; }

    public ViewUserPermissionsReply(String username, String sessionToken) throws SQLException {
        if (UserSession.hasPermission(sessionToken, PermissionType.editUsers)){
            retrieveUserPermissions(username);
        }else{
            this.errorMessage = ReplyError.userNotPermitted;
        }
    }

    private void retrieveUserPermissions(String username) throws SQLException {
        String query = "SELECT * FROM permissions WHERE username = '"+username+"';";
        Statement statement = DBConnection.getInstance().createStatement();
        ResultSet userRows = statement.executeQuery(query);
        if (userRows.next()){
            try {
                boolean p1 = userRows.getBoolean(2);
                boolean p2 = userRows.getBoolean(3);
                boolean p3 = userRows.getBoolean(4);
                boolean p4 = userRows.getBoolean(5);
                userPermissions = new UserPermissions(p1,p2,p3,p4);
                statement.close();
                this.success = true;
            } catch (SQLException exception){
                System.out.println("errorCol");
            }
        }
    }
}
