import java.io.Serializable;

public class BillboardRequest implements Serializable {
    String request;
    String sessionToken;
    Object[] data;

    public BillboardRequest(String request, Object[] data, String sessionToken){
        this.request = request;
        this.sessionToken = sessionToken;
        this.data = data;
    }
}
