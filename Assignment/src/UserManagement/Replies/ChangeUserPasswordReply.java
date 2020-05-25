package UserManagement.Replies;

import Database.DBConnection;
import User.PermissionType;
import User.ServerUserSession;
import UserManagement.DataSecurity;
import UserManagement.Requests.EditUserPropertyRequest;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;

public class ChangeUserPasswordReply extends Reply{

    public ChangeUserPasswordReply(EditUserPropertyRequest editUserPropertyRequest, String sessionToken) throws SQLException, NoSuchAlgorithmException {
        super(sessionToken);
        String username = editUserPropertyRequest.getUsername();
        String password = editUserPropertyRequest.getPassword();
        int minPasswordLength = 8;
        boolean clientIsTargetUser = username.equals(ServerUserSession.getUsername(sessionToken));
        boolean userIsAdmin = ServerUserSession.hasPermission(sessionToken, PermissionType.editUsers);

        if (!sessionExpired)
        {
            if ((clientIsTargetUser || userIsAdmin)){
                changePassword(username, password);
            } else {
                this.errorMessage = ReplyError.userNotPermitted;
            }
        }
    }

    private void changePassword(String username, String password) throws NoSuchAlgorithmException {
        String salt = DataSecurity.randomString();
        String saltedPassword = DataSecurity.hash(password+salt);

        try {
            String query = "UPDATE user SET password='"+saltedPassword+"', salt='"+salt+"' WHERE username='"+username+"';";
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
