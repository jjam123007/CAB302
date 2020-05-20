import Billboard.Billboards;

import java.io.Serializable;

public class BillboardRequest implements Serializable {
    Billboards request;
    String sessionToken;
    Object[] data;

    public BillboardRequest(Billboards request, Object[] data, String sessionToken){
        this.request = request;
        this.sessionToken = sessionToken;
        this.data = data;
    }
}
