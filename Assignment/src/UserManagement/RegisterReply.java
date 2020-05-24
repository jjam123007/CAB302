package UserManagement;

import Database.DBConnection;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterReply implements Serializable {
    private boolean success = false;
    private String errorMessage = null;
    private static RegisterRequest registerRequest;

    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }

    public RegisterReply(RegisterRequest registerRequest) throws SQLException, NoSuchAlgorithmException {
        this.registerRequest = registerRequest;
        String sessionToken = registerRequest.getSessionToken();
        if (UserSession.hasPermission(sessionToken, PermissionType.editUsers))
        {
            registerUser();
        } else {
            this.errorMessage = "User does not have permission";
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
        int editUsersPerm = registerRequest.getPermissions().canEditUsers() ? 1 : 0;
        int createBillboardsPerm = registerRequest.getPermissions().canCreateBillboards() ? 1 : 0;
        int editBillboardsPerm = registerRequest.getPermissions().canEditBillboards() ? 1 : 0;
        int scheduleBillboardsPerm = registerRequest.getPermissions().canScheduleBillboards() ? 1 : 0;

        String registerQuery = "INSERT INTO permissions values('"+username+"', " +
                "'"+createBillboardsPerm+"'," +
                " '"+editBillboardsPerm+"'," +
                " '"+scheduleBillboardsPerm+"', " +
                "'"+editUsersPerm+"');";

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
