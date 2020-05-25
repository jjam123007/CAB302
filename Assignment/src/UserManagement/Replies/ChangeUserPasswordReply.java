package UserManagement.Replies;

import Database.DBConnection;
import User.PermissionType;
import User.UserSession;
import UserManagement.DataSecurity;
import UserManagement.Requests.EditUserPropertyRequest;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;

public class ChangeUserPasswordReply extends Reply{

    public ChangeUserPasswordReply(EditUserPropertyRequest editUserPropertyRequest, String sessionToken) throws SQLException, NoSuchAlgorithmException {
        String username = editUserPropertyRequest.getUsername();
        String password = editUserPropertyRequest.getPassword();

        boolean clientIsTargetUser = username.equals(UserSession.getUsername(sessionToken));
        boolean userIsAdmin = UserSession.hasPermission(sessionToken, PermissionType.editUsers);
        if (username.contains(" ")){
            this.errorMessage = "Password cannot contain spaces!";
        } else if (clientIsTargetUser || userIsAdmin){
            changePassword(username, password);
        } else {
            this.errorMessage = ReplyError.userNotPermitted;
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
