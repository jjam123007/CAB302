import java.io.Serializable;

public class BillboardRequest implements Serializable {
    String request;
    String token;
    Object[] data;
    public BillboardRequest(String request, String token, Object[] data){
        this.request = request;
        this.token = token;
        this.data = data;
    }
}
