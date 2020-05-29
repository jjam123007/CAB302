package BillboardViewer.Requests;

import BillboardViewer.BillboardViewer;
import Networking.Request;
import User.ClientUser;
import UserManagement.Requests.UserManagementRequestType;

import java.io.IOException;
import java.io.Serializable;

public class ViewerRequest extends Request implements Serializable {
    private ViewerRequestType requestType;

    public ViewerRequestType getRequestType() {
        return requestType;
    }

    // Constructor
    public ViewerRequest(ViewerRequestType requestType) throws IOException {
        super();
        this.requestType = requestType;
        sendRequest(this);
    }

}
