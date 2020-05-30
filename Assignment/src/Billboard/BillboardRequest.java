package Billboard;

import Networking.Request;

import java.io.IOException;
import java.io.Serializable;

/**
 * This class used to serialize the data needed
 *  @author Jun Chen(n10240977)&Haoze He(n10100351)
 */
public class BillboardRequest extends Request implements Serializable {
    BillboardRequestType request;
    String sessionToken;
    Object[] data;

    /**
     *
     * @return the request
     */
    public BillboardRequestType getRequest() {
        return request;
    }

    /**
     *
     * @return these data
     */
    public String getSessionToken() { return sessionToken; }
    public Object[] getData() {
        return data;
    }

    /**
     *
     * @param request
     * @param data
     * @param sessionToken
     */
    public BillboardRequest(BillboardRequestType request, Object[] data, String sessionToken) throws IOException {
        super();
        this.request = request;
        this.sessionToken = sessionToken;
        this.data = data;
        sendRequest(this);
    }


}
