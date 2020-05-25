package UserManagement.Replies;

import Database.DBConnection;
import User.PermissionType;
import User.UserPermissions;
import User.UserSession;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewUsersReply extends Reply implements Serializable {

    private List<Object> userDataTable;
    public List<Object> getUserDataTable() {
        return userDataTable;
    }

    public ViewUsersReply(String sessionToken) throws SQLException {
        if (UserSession.hasPermission(sessionToken, PermissionType.editUsers)){
           retrieveUsers();
        }else{
            this.errorMessage = ReplyError.userNotPermitted;
        }
    }

    private void retrieveUsers() {
        try {
            String query = "SELECT * FROM permissions";
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet userRows = statement.executeQuery(query);
            userDataTable = new ArrayList<>();
            ArrayList<Object> userData = new ArrayList<>();
            while(userRows.next()){
                String username = userRows.getString(1);
                userDataTable.add(username);
            }
            statement.close();
            this.success = true;
        } catch (SQLException exception){
            exception.printStackTrace();
            this.errorMessage = exception.getMessage();
        }
    }

    //private HashMap<String, UserPermissions> userPermissionsHashMap = new HashMap<>();
    /*String username = userRows.getString(1);
                boolean p1 = userRows.getBoolean(2);
                boolean p2 = userRows.getBoolean(3);
                boolean p3 = userRows.getBoolean(4);
                boolean p4 = userRows.getBoolean(5);
                UserPermissions userPermissions = new UserPermissions(p1,p2,p3,p4);
                userPermissionsHashMap.put(username, userPermissions);*/



}
