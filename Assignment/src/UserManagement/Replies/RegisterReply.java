package UserManagement.Replies;

import Database.DBConnection;
import User.PermissionType;
import User.ServerUserSession;
import UserManagement.DataSecurity;
import UserManagement.Requests.RegisterRequest;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Nikolai Taufao | N10481087
 */
public class RegisterReply extends Reply implements Serializable {
    private static RegisterRequest registerRequest;

    /**
     * Register the user specified in the request to the database.
     * @param registerRequest
     * @param sessionToken
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public RegisterReply(RegisterRequest registerRequest, String sessionToken) throws SQLException, NoSuchAlgorithmException {
        super(sessionToken);
        if (tokenExpired) return;

        this.registerRequest = registerRequest;
        if (ServerUserSession.hasPermission(sessionToken, PermissionType.editUsers)) {
            registerUser();
        } else {
            this.errorMessage = ReplyError.userNotPermitted;
        }
    }

    /**
     * If the user is able to be registered, add their username, password and permissions to the database.
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    private void registerUser() throws SQLException, NoSuchAlgorithmException {
        Statement statement = DBConnection.getInstance().createStatement();
        String username = registerRequest.getUsername();

        if (canRegister(statement)){
            registerUserToDB(statement, username);
            registerUserPermsToDB(statement, username);
            this.success = true;
        }
        statement.close();
    }


    /**
     * Return true only if the specified username does not exist in the database.
     * @param statement sql database connection statement.
     * @return whether the specified user can be registered.
     * @throws SQLException
     */
    private boolean canRegister(Statement statement) throws SQLException {
        String username = registerRequest.getUsername();
        String userExistsQuery = ("SELECT username FROM users WHERE username='" + username + "';");
        ResultSet user = statement.executeQuery(userExistsQuery);
        boolean userExists = user.next();
        if (!userExists){
            return true;
        } else{
            this.errorMessage = "Username already exists! Please use another.";
            return false;
        }
    }

    /**
     * Store the target user's username, salted password and password salt in the database.
     * @param statement sql database connection statement.
     * @param username
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    private void registerUserToDB(Statement statement, String username) throws SQLException, NoSuchAlgorithmException {
        String password = registerRequest.getPassword();
        String salt = DataSecurity.randomString();
        String saltedPassword = DataSecurity.hash(password+salt);
        String registerQuery = "INSERT INTO users values('"+username+"', '"+saltedPassword+"', '"+salt+"');";
        statement.executeQuery(registerQuery);
    }


    /**
     * Store the target user's permissions in the database.
     * @param statement
     * @param username
     * @throws SQLException
     */
    private void registerUserPermsToDB(Statement statement, String username) throws SQLException {
        int p1 = registerRequest.getPermissions().canCreateBillboards() ? 1 : 0;
        int p2 = registerRequest.getPermissions().canEditBillboards() ? 1 : 0;
        int p3 = registerRequest.getPermissions().canScheduleBillboards() ? 1 : 0;
        int p4 = registerRequest.getPermissions().canEditUsers() ? 1 : 0;

        String registerQuery = "INSERT INTO permissions values('"+username+"', " +
                "'"+p1+"'," +
                " '"+p2+"'," +
                " '"+p3+"', " +
                "'"+p4+"');";

        statement.executeQuery(registerQuery);
    }


}
