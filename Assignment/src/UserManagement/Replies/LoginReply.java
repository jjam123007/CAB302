package UserManagement.Replies;

import Database.DBConnection;
import UserManagement.Requests.LoginRequest;
import User.PermissionType;
import User.ServerUserSession;
import User.UserPermissions;
import UserManagement.DataSecurity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * @author Nikolai Taufao | N10481087
 */
public class LoginReply implements Serializable{

    private Boolean success = false;
    private String errorMessage = null;
    private String sessionToken = null;

    public String getUsername() {
        return username;
    }

    private String username;
    private UserPermissions permissions = null;

    private static LoginRequest loginRequest;

    public Boolean isSuccess() { return success; }
    public String getErrorMessage() {return errorMessage;}
    public String getSessionToken() {return sessionToken;}
    public UserPermissions getPermissions() { return permissions; }


    /**
     * Send back the client's username, password and permissions if the request credentials are correct.
     * @param loginRequest the login request.
     * @throws SQLException
     */
    public LoginReply(LoginRequest loginRequest) throws SQLException  {
       this.loginRequest = loginRequest;
       this.username = loginRequest.getUsername();
       try {
           String username = loginRequest.getUsername();
           String dbPasswordQuery = ("SELECT password, salt FROM users WHERE username='" + username + "';");

           Statement statement = DBConnection.getInstance().createStatement();
           ResultSet passwordResult = statement.executeQuery(dbPasswordQuery);

           boolean userExists = passwordResult.next();
           if (userExists) {
               String requestPassword = saltPassword(passwordResult);
               String storedPassword = passwordResult.getString("password");
               checkPassword(requestPassword, storedPassword);
           } else {
               this.errorMessage = "User does not exist!";
           }
           statement.close();

       }catch (Exception exception){
           this.errorMessage = ReplyError.databaseError;
           exception.printStackTrace();
       }
   }

    /***
     * Salt and hash the user's password from the database and return it.
     * @param passwordResult the sql result set from the user table.
     * @return the hashed salted password.
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
   public String saltPassword(ResultSet passwordResult) throws SQLException, NoSuchAlgorithmException {
        String password = loginRequest.getPassword();
        String salt = passwordResult.getString("salt");
        String saltedPassword = DataSecurity.hash(password+salt);
        return saltedPassword;
   }

    /***
     * Retrieve the user's permissions if the client's password matches the salted password in the database.
     * @param requestPassword the password sent by the client which was hashed and salted.
     * @param saltedPassword the salted password stored in the database.
     * @throws SQLException
     */

   public void checkPassword(String requestPassword, String saltedPassword) throws SQLException {
       if (requestPassword.equals(saltedPassword)){
           this.sessionToken = DataSecurity.randomString();
           retrievePermissions();
           ServerUserSession.addSession(this.sessionToken, loginRequest.getUsername());
           ServerUserSession.getUsername(this.sessionToken);
       }else{
           this.errorMessage = "Incorrect password.";
       }
   }

    /***
     * Retrieve the user's permissions from the database and send it to the client.
     * @throws SQLException
     */
   public void retrievePermissions() throws SQLException {
        String username = loginRequest.getUsername();
        String permissionsQuery = ("SELECT * FROM permissions WHERE username='" + username + "';");
        try{
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet getDBUserPermissions = statement.executeQuery(permissionsQuery);
            getDBUserPermissions.next();

            boolean editUsersPerm = getDBUserPermissions.getBoolean(PermissionType.editUsers);
            boolean createBillboardsPerm = getDBUserPermissions.getBoolean(PermissionType.createBillboards);
            boolean editBillboardsPerm = getDBUserPermissions.getBoolean(PermissionType.editBillboards);
            boolean scheduleBillboardsPerm = getDBUserPermissions.getBoolean(PermissionType.scheduleBillboards);

            this.permissions = new UserPermissions(createBillboardsPerm,editBillboardsPerm,scheduleBillboardsPerm,editUsersPerm);
            this.success = true;
        } catch (SQLException sqlException){
            this.errorMessage = ReplyError.databaseError;
        }

   }

}
