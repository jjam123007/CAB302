
package Billboard;

import java.io.Serializable;

public class BillboardReply implements Serializable {
    String message;

    public String getMessage() {
        return message;
    }
    public BillboardReply(String message){
        this.message = message;
    }
}