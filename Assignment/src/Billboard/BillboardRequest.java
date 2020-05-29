package Billboard;

import Networking.Request;

import java.io.IOException;
import java.io.Serializable;

public class BillboardRequest extends Request implements Serializable {
    BillboardRequestType request;
    String sessionToken;
    Object[] data;

    public BillboardRequestType getRequest() {
        return request;
    }
    public String getSessionToken() { return sessionToken; }
    public Object[] getData() {
        return data;
    }

    public BillboardRequest(BillboardRequestType request, Object[] data, String sessionToken) throws IOException {
        super();
        this.request = request;
        this.sessionToken = sessionToken;
        this.data = data;
        sendRequest(this);
    }


}
