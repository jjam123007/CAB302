package BillboardViewer;

import User.ClientUser;
import UserManagement.Requests.UserManagementRequestType;

import java.io.Serializable;

public class ViewerRequest implements Serializable {
    private ViewerRequestType requestType;
    private Object request = null;

    public ViewerRequestType getRequestType() {
        return requestType;
    }
    public Object getRequest() {
        return request;
    }

    // Constructor
    public ViewerRequest(ViewerRequestType requestType){
        this.requestType = requestType;
    }

    public ViewerRequest(ViewerRequestType requestType, Object request){
        this.requestType = requestType;
        this.request = request;
    }
}
