package UserManagement;

import Database.DBConnection;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginReply implements Serializable{
    private Boolean success = false;
    private String errorMessage = null;
    private String sessionToken = null;
    private UserPermissions permissions = null;

    private static LoginRequest loginRequest;

    public Boolean isSuccess() { return success; }
    public String getErrorMessage() {return errorMessage;}
    public String getSessionToken() {return sessionToken;}
    public UserPermissions getPermissions() { return permissions; }

    public LoginReply(LoginRequest loginRequest) throws SQLException  {
       this.loginRequest = loginRequest;

       try {
           String username = loginRequest.getUsername();
           String dbPasswordQuery = ("SELECT password, salt FROM user WHERE username='" + username + "';");

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
       }
   }

   public String saltPassword(ResultSet passwordResult) throws SQLException, NoSuchAlgorithmException {

        String password = loginRequest.getPassword();
        String salt = passwordResult.getString("salt");
        String saltedPassword = DataSecurity.hash(password+salt);
       return saltedPassword;
   }

   public void checkPassword(String requestPassword, String saltedPassword) throws SQLException {
       if (requestPassword.equals(saltedPassword)){
           this.success = true;
           this.sessionToken = DataSecurity.randomString();
           retrievePermissions();
           UserSession.addSession(this.sessionToken, loginRequest.getUsername());
       }else{
           this.errorMessage = "Incorrect password.";
       }
   }

   public void retrievePermissions() throws SQLException {
        String username = loginRequest.getUsername();
        String permissionsQuery = ("SELECT * FROM permissions WHERE username='" + username + "';");
        Statement statement = DBConnection.getInstance().createStatement();
        ResultSet getDBUserPermissions = statement.executeQuery(permissionsQuery);
        getDBUserPermissions.next();

        boolean editUsersPerm = getDBUserPermissions.getBoolean(PermissionType.editUsers);
        boolean createBillboardsPerm = getDBUserPermissions.getBoolean(PermissionType.createBillboards);
        boolean editBillboardsPerm = getDBUserPermissions.getBoolean(PermissionType.editBillboards);
        boolean scheduleBillboardsPerm = getDBUserPermissions.getBoolean(PermissionType.scheduleBillboards);

        this.permissions = new UserPermissions(createBillboardsPerm,editBillboardsPerm,scheduleBillboardsPerm,editUsersPerm);
       System.out.println( this.permissions);
   }

}
