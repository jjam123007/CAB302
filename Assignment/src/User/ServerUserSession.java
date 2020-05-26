package User;

import Database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;


public final class ServerUserSession {
    //Hash map maps session token to User object
    private static final HashMap<String, ServerUser> users = new HashMap<>();
    //Expiration time (hours converted to milliseconds)
    private static int expirationTime = 24 * (3600000);//hours * milliseconds in an hour;

    public static String getUsername(String sessionToken){
        try {
            return users.get(sessionToken).getUsername();
        } catch(Exception e) {
            return null;
        }
    }
    public static boolean sessionExists(String sessionToken){
        return users.containsKey(sessionToken);
    }
    public static void addSession(String sessionToken, String username){
        Date currentDate = new Date();
        Long currentTime = currentDate.getTime();
        ServerUser user = new ServerUser(username, currentTime);
        users.put(sessionToken, user);
    }

    public static void removeSession(String sessionToken){
        users.remove(sessionToken);
    }

    public static boolean hasPermission(String sessionToken, String permission) throws SQLException {
        if (isValid(sessionToken)){
            ServerUser user = users.get(sessionToken);
            String query = ("SELECT "+permission+" FROM permissions WHERE username='"+user.getUsername()+"';");
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
            ServerUser user = users.get(sessionToken);
            Long inactiveTime = (currentTime - user.getLastSessionUseTime());
            if (inactiveTime < expirationTime){
                user.setLastSessionUseTime(currentTime);
                return true;
            }else{
                System.out.println("Token expired!");
                removeSession(sessionToken);
                return false;
            }
        } else{

            return false;
        }
    }
}
