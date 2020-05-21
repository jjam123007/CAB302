package Billboard;

import java.io.Serializable;

public class BillboardRequest implements Serializable {
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

    public BillboardRequest(BillboardRequestType request, Object[] data, String sessionToken){
        this.request = request;
        this.sessionToken = sessionToken;
        this.data = data;
    }


}
