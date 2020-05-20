package Billboard;

import java.io.Serializable;

public class Delete implements Serializable {
    private final int BillboardID;

    public Delete (int BillboardID){
        this.BillboardID = BillboardID;
    }


    public Object[] getVal() {
        Object data[] = new Object[1];
        data[0] = BillboardID;
        return data;
    }

}
