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

public class RegisterReply extends Reply implements Serializable {
    private static RegisterRequest registerRequest;

    public RegisterReply(RegisterRequest registerRequest, String sessionToken) throws SQLException, NoSuchAlgorithmException {
        super(sessionToken);
        this.registerRequest = registerRequest;
        if (!sessionExpired) {
            if (ServerUserSession.hasPermission(sessionToken, PermissionType.editUsers)) {
                registerUser();
            } else {
                this.errorMessage = ReplyError.userNotPermitted;
            }
        }
    }

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

    private void registerUserToDB(Statement statement, String username) throws SQLException, NoSuchAlgorithmException {
        String password = registerRequest.getPassword();
        String salt = DataSecurity.randomString();
        String saltedPassword = DataSecurity.hash(password+salt);
        String registerQuery = "INSERT INTO user values('"+username+"', '"+saltedPassword+"', '"+salt+"');";
        statement.executeQuery(registerQuery);
    }

    private void registerUserPermsToDB(Statement statement, String username) throws SQLException {
        int p1 = registerRequest.getPermissions().canEditUsers() ? 1 : 0;
        int p2 = registerRequest.getPermissions().canCreateBillboards() ? 1 : 0;
        int p3 = registerRequest.getPermissions().canEditBillboards() ? 1 : 0;
        int p4 = registerRequest.getPermissions().canScheduleBillboards() ? 1 : 0;

        String registerQuery = "INSERT INTO permissions values('"+username+"', " +
                "'"+p2+"'," +
                " '"+p3+"'," +
                " '"+p4+"', " +
                "'"+p1+"');";

        statement.executeQuery(registerQuery);
    }

    private boolean canRegister(Statement statement) throws SQLException {
        String username = registerRequest.getUsername();
        String userExistsQuery = ("SELECT username FROM user WHERE username='" + username + "';");
        ResultSet user = statement.executeQuery(userExistsQuery);
        boolean userExists = user.next();
        if (!userExists){
            System.out.println("user does not exist");

            return true;
        } else{
            System.out.println("user exists");
            this.errorMessage = "Username already exists! Please use another.";
            return false;
        }
    }

}
