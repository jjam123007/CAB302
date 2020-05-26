package BillboardViewer.Requests;

import BillboardViewer.BillboardViewer;
import User.ClientUser;
import UserManagement.Requests.UserManagementRequestType;

import java.io.Serializable;

public class ViewerRequest implements Serializable {
    private ViewerRequestType requestType;

    public ViewerRequestType getRequestType() {
        return requestType;
    }

    // Constructor
    public ViewerRequest(ViewerRequestType requestType){
        this.requestType = requestType;
    }

}
