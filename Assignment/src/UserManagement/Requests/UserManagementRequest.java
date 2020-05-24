package UserManagement.Requests;

import User.ClientUser;
import UserManagement.UserManagementRequestType;

import java.io.Serializable;


public class UserManagementRequest implements Serializable {
    private String sessionToken;
    private UserManagementRequestType requestType;
    private Object request = null;

    public String getSessionToken() {
        return sessionToken;
    }
    public UserManagementRequestType getRequestType() {
        return requestType;
    }
    public Object getRequest() {
        return request;
    }

    public UserManagementRequest(UserManagementRequestType requestType){
        this.sessionToken = ClientUser.getToken();
        this.requestType = requestType;
    }

    public UserManagementRequest(UserManagementRequestType requestType, Object request){
        this.sessionToken = ClientUser.getToken();
        this.requestType = requestType;
        this.request = request;
    }
}
