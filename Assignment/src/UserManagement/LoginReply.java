package UserManagement;

import Database.DBConnection;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginReply implements Serializable{
    private Boolean success = false;
    private String errorMessage = null;
    private String sessionToken = null;

    public Boolean isSuccess() { return success; }
    public String getErrorMessage() {return errorMessage;}
    public String getSessionToken() {return sessionToken;}

    public LoginReply(LoginRequest loginRequest) throws SQLException  {
       try {
           String username = loginRequest.getUsername();
           String password = loginRequest.getPassword();
           String query = ("SELECT password FROM user WHERE username='" + username + "';");
           Statement statement = DBConnection.getInstance().createStatement();
           ResultSet user = statement.executeQuery(query);

           Boolean userExists = user.next();
           if (userExists) {
               String serverPassword = user.getString("password");
               if (password.equals(serverPassword)){
                   this.success = true;
                   this.sessionToken = DataSecurity.randomString();
                   UserSession.addSession(this.sessionToken,username);
               }else{
                   this.errorMessage = "Incorrect password.";
               }

           } else {
               this.errorMessage = "User does not exist!";
           }
           statement.close();

       }catch (Exception e){
           System.out.println(e);
       }
   }


}
