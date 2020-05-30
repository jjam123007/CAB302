package User;

import Database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.function.BooleanSupplier;

/**
 * @author Nikolai Taufao | N10481087
 */
public final class ServerUserSession {
    //Map a session token to its user.
    private static final HashMap<String, ServerUser> users = new HashMap<>();

    private static int millisecondsInHours = 3600000;
    private static int hoursUntilExpiration = 24;
    //The expiration time in milliseconds.
    private static int expirationTime = hoursUntilExpiration*millisecondsInHours;

    /**
     * Return the username associated with the given session token.
     * @param sessionToken
     * @return a username.
     */
    public static String getUsername(String sessionToken){
        try {
            return users.get(sessionToken).getUsername();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return true if the session token is valid.
     * @param sessionToken
     * @return true or false.
     */
    public static boolean sessionExists(String sessionToken){
        return users.containsKey(sessionToken);
    }

    /**
     * Map the session token of a user to a newly created user object which contains their username
     * and the current date.
     * @param sessionToken
     * @param username
     */
    public static void addSession(String sessionToken, String username){
        Date currentDate = new Date();
        Long currentTime = currentDate.getTime();
        ServerUser user = new ServerUser(username, currentTime);
        users.put(sessionToken, user);
    }

    /**
     * Remove the session token and it's associated user from the server.
     * @param sessionToken
     */
    public static void removeSession(String sessionToken){
        users.remove(sessionToken);
    }

    /**
     * Return true if the provided session token is valid and the user associated with the token
     * has the specified permission.
     * @param sessionToken
     * @param permission
     * @return true or false.
     * @throws SQLException
     */
    public static boolean hasPermission(String sessionToken, String permission) throws SQLException {
        if (isValid(sessionToken)){
            ServerUser user = users.get(sessionToken);
            String query = ("SELECT "+permission+" FROM permissions WHERE username='"+user.getUsername()+"';");
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet row = statement.executeQuery(query);
            row.next();
            //Get the boolean value of the user's permission from the database.
            boolean isPermitted = row.getBoolean(permission);
            statement.close();
            if (isPermitted) {return true;} else {return false;}
        }else{
            return false;
        }
    }

    /**
     * Check if the provided session token is stored within the server and has
     * not yet expired.
     * @param sessionToken
     * @return
     */
    private static boolean isValid(String sessionToken){
        if (users.containsKey(sessionToken)) {
            Date currentDate = new Date();
            //The current time in milliseconds,
            Long currentTime = currentDate.getTime();
            ServerUser user = users.get(sessionToken);
            //The inactive time in milliseconds.
            Long inactiveTime = (currentTime - user.getLastSessionUseTime());
            Boolean sessionExpired = inactiveTime >= expirationTime;

            if (!sessionExpired){
                //Store the current time as the last time the user has interacted with the server.
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
