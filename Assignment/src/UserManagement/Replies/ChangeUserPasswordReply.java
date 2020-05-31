package UserManagement.Replies;

import Database.DBConnection;
import Networking.Reply;
import Networking.ReplyError;
import User.PermissionType;
import User.ServerUserSession;
import UserManagement.DataSecurity;
import UserManagement.Requests.EditUserPropertyRequest;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * @author Nikolai Taufao | N10481087
 */
public class ChangeUserPasswordReply extends Reply {

    /***
     * Changed the password of the user specified in the request only if the request client user is an admin.
     * @param editUserPropertyRequest object containing the user and their new password.
     * @param sessionToken
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public ChangeUserPasswordReply(EditUserPropertyRequest editUserPropertyRequest, String sessionToken) throws SQLException, NoSuchAlgorithmException {
        super(sessionToken);
        if (tokenExpired) return;

        String username = editUserPropertyRequest.getUsername();
        String password = editUserPropertyRequest.getPassword();
        //Check if the user that send the request is trying to change their own password.
        boolean clientIsTargetUser = username.equals(ServerUserSession.getUsername(sessionToken));
        //Check if the user that send the request is an admin.
        boolean clientUserIsAdmin = ServerUserSession.hasPermission(sessionToken, PermissionType.editUsers);
        if ((clientIsTargetUser || clientUserIsAdmin)){
            changePassword(username, password);
        } else {
            this.errorMessage = ReplyError.userNotPermitted;
        }
    }

    /**
     * Change the password associated with the username in the database.
     * @param username
     * @param password
     * @throws NoSuchAlgorithmException
     */
    private void changePassword(String username, String password) throws NoSuchAlgorithmException {
        String salt = DataSecurity.randomString();
        String saltedPassword = DataSecurity.hash(password+salt);

        try {
            String changeUserPassword = "UPDATE users SET password='"+saltedPassword+"', salt='"+salt+"' WHERE username='"+username+"';";
            Statement statement = DBConnection.getInstance().createStatement();
            statement.executeQuery(changeUserPassword);
            statement.close();
            this.success = true;
        } catch (SQLException exception){
            exception.printStackTrace();
            this.errorMessage = ReplyError.databaseError;
        }
    }

}
