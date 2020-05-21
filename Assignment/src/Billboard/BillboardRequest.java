package Billboard;

import java.io.Serializable;

public class BillboardRequest implements Serializable {
    BillboardRequestType request;
    String sessionToken;
    Object[] data;

    public String getSessionToken() {
        return sessionToken;
    }

    public BillboardRequestType getRequest() {
        return request;
    }

    public Object[] getData() {
        return data;
    }

    public BillboardRequest(BillboardRequestType request, Object[] data, String sessionToken){
        this.request = request;
        this.sessionToken = sessionToken;
        this.data = data;
    }


}
