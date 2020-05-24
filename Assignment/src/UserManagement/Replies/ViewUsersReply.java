package UserManagement.Replies;

import Database.DBConnection;
import User.PermissionType;
import User.UserSession;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViewUsersReply extends Reply implements Serializable {

    public List<Object> getUsernames() {
        return usernames;
    }
    private List<Object> usernames;

    public ViewUsersReply(String sessionToken) throws SQLException {
        if (UserSession.hasPermission(sessionToken, PermissionType.editUsers)){
           getUsers();
        }else{
            this.errorMessage = ReplyError.userNotPermitted;
        }
    }

    private void getUsers() {
        try {
            String query = "SELECT username FROM user";
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet usernameRows = statement.executeQuery(query);
            usernames = new ArrayList<>();
            String username;
            while(usernameRows.next()){
                username = usernameRows.getString("username");
                usernames.add(username);
            }
            statement.close();
            this.success = true;
        } catch (SQLException exception){
            this.errorMessage = exception.getMessage();
        }
    }

}
