package UserManagement.Requests;

import User.ClientUser;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Nikolai Taufao | N10481087
 */
public class UserManagementRequest extends Request implements Serializable {
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

    /**
     * Send a request to the server that involves retrieving details of registered users or managing user sessions.
     * @param requestType the type of user management request.
     */
    public UserManagementRequest(UserManagementRequestType requestType) throws IOException {
        super();
        this.sessionToken = ClientUser.getToken();
        this.requestType = requestType;
        sendRequest(this);
    }

    /**
     * Send a request to the server that involves adding new or altering users and their details.
     * @param requestType the type of user management request.
     * @param request the object holding the request data, such as target user details.
     */
    public UserManagementRequest(UserManagementRequestType requestType, Object request) throws IOException {
        super();
        this.sessionToken = ClientUser.getToken();
        this.requestType = requestType;
        this.request = request;
        sendRequest(this);
    }
}
