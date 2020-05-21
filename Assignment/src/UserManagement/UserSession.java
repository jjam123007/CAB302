package UserManagement;

import Database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public final class UserSession {
    //Hash map maps session token to User object
    private static final HashMap<String, User> users = new HashMap<String, User>();
    //Expiration time (hours converted to milliseconds)
    private static int expirationTime = 24 * (3600000);

    private static class User{
        public String username;
        public Long lastSessionUseTime;
        public User(String username, Long lastSessionUseTime) {
            this.username = username;
            this.lastSessionUseTime = lastSessionUseTime;
        }
    }

    public static void addSession(String sessionToken, String username){
        Date currentDate = new Date();
        Long currentTime = currentDate.getTime();
        User user = new User(username, currentTime);
        users.put(sessionToken, user);
    }

    public static void removeSession(String sessionToken){
        users.remove(sessionToken);
    }

    public static boolean hasPermission(String sessionToken, String permission) throws SQLException {
        if (isValid(sessionToken)){
            User user = users.get(sessionToken);
            String query = ("SELECT "+permission+" FROM permissions WHERE username='"+user.username+"';");
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet row = statement.executeQuery(query);
            row.next();
            boolean isPermitted = row.getBoolean(permission);
            statement.close();
            if (isPermitted) {return true;} else {return false;}
        }else{
            return false;
        }
    }

    private static boolean isValid(String sessionToken){
        if (users.containsKey(sessionToken)) {
            Date currentDate = new Date();
            Long currentTime = currentDate.getTime();
            User user = users.get(sessionToken);

            if ((currentTime - user.lastSessionUseTime) < expirationTime){
                return true;
            }else{
                removeSession(sessionToken);
                return false;
            }
        } else{
            return false;
        }
    }
}
