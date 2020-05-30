
package Billboard;

import Networking.Reply;

import java.io.Serializable;

/**
 * @author Jun Chen(n10240977)&Haoze He(n10100351)
 * setter function to set value for BillboardReply elements and variables
 */
public class BillboardReply implements Serializable {
    String message;

    public String getMessage() {return message;}

    public BillboardReply(String message){
        this.message = message;
    }
}