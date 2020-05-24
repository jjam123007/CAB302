package UserManagement;

import java.io.Serializable;


public class UserManagementRequest implements Serializable {
    String sessionToken;
    UserManagementRequestType requestType;
    Object[] data;
    public UserManagementRequest(UserManagementRequestType requestType, Object[] data){
        this.sessionToken = ClientUser.getToken();
        this.requestType = requestType;
        this.data = data;
    }
}
