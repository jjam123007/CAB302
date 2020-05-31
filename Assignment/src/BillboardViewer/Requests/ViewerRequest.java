package BillboardViewer.Requests;

import BillboardViewer.BillboardViewer;
import User.ClientUser;
import UserManagement.Requests.UserManagementRequestType;

import java.io.Serializable;

/**
 * A class containing the requests that the viewer could make
 * (Only 1 for now, but can be expanded later on)
 *
 * @author William Tran (10306234)
 * @see ViewerRequestType
 */
public class ViewerRequest implements Serializable {
    // The type of request
    private ViewerRequestType requestType;

    /**
     * Return the request type of the request object
     * @return The request type
     */
    public ViewerRequestType getRequestType() {
        return requestType;
    }

    /**
     * The constructor of the object
     * @param requestType Specifies the request type
     */
    public ViewerRequest(ViewerRequestType requestType){
        this.requestType = requestType;
    }

}
