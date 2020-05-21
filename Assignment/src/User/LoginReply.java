package User;

import Database.DBConnection;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginReply implements Serializable{
    private Boolean success = false;
    private String errorMessage = null;


    public Boolean Success() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LoginReply(LoginRequest loginRequest) throws SQLException  {
       try {
           String username = loginRequest.getUsername();
           String password = loginRequest.getPassword();
           String query = ("SELECT id, username, password FROM users WHERE username='" + username + "';");
           Statement statement = DBConnection.getInstance().createStatement();
           ResultSet user = statement.executeQuery(query);

           Boolean userExists = user.next();
           if (userExists) {
               String serverPassword = user.getString("password");
               if (password.equals(serverPassword)){
                   this.success = true;
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
