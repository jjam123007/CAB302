package UserManagement.Replies;

import Database.DBConnection;
import User.PermissionType;
import User.UserSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RemoveUserReply extends Reply {

    public RemoveUserReply(String username, String sessionToken) throws SQLException {
        if (UserSession.hasPermission(sessionToken, PermissionType.editUsers)){
            removeUser(username);
        } else{
            errorMessage = ReplyError.userNotPermitted;
        }
    }

    private void removeUser(String username) {
        try {
            String query = "DELETE FROM user WHERE username = '"+username+"';";
            Statement statement = DBConnection.getInstance().createStatement();
            statement.executeQuery(query);
            statement.close();
            this.success = true;
        } catch (SQLException exception) {
            this.errorMessage = "User has already been removed.";
        }

    }


}
