package UserManagement.Replies;

import Database.DBConnection;
import User.PermissionType;
import User.UserPermissions;
import User.UserSession;
import UserManagement.Requests.EditUserPropertyRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EditUserPermisionsReply extends Reply{
    public EditUserPermisionsReply(EditUserPropertyRequest editUserPropertyRequest, String sessionToken) throws SQLException {
        String username = editUserPropertyRequest.getUsername();
        UserPermissions userPermissions = editUserPropertyRequest.getPermissions();

        if (UserSession.hasPermission(sessionToken, PermissionType.editUsers))
        {
            checkUserPermissions(username, userPermissions, sessionToken);
        } else {
            this.errorMessage = ReplyError.userNotPermitted;
        }
    }

    private void checkUserPermissions(String username, UserPermissions userPermissions, String sessionToken) {
        String clientUser = UserSession.getUsername(sessionToken);

        if (username.equals(clientUser) && userPermissions.canEditUsers() == false){
            this.errorMessage="You cant remove permission 'edit users' from your own account!";
        }else{
            changeUserPermissions(username, userPermissions);
        }

    }

    private void changeUserPermissions(String username, UserPermissions userPermissions) {
        try {
            int p1 = userPermissions.canCreateBillboards() ? 1 : 0;
            int p2 = userPermissions.canEditBillboards() ? 1 : 0;
            int p3 = userPermissions.canScheduleBillboards()? 1 : 0;
            int p4 = userPermissions.canEditUsers()? 1 : 0;

            String query = "UPDATE permissions SET " +
                    "createBillboards='"+p1+"'," +
                    "editBillboards='"+p2+"'," +
                    "scheduleBillboards='"+p3+"'," +
                    "editUsers='"+p4+"' WHERE username='"+username+"'; ";

            Statement statement = DBConnection.getInstance().createStatement();
            statement.executeQuery(query);
            statement.close();
            this.success = true;

        } catch (SQLException exception){
            exception.printStackTrace();
            this.errorMessage = exception.getMessage();
        }
    }

}
