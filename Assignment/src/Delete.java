import java.io.Serializable;

public class Delete implements Serializable {
    private final String BillboardID;


    public Delete (String BillboardName){
        this.BillboardID = BillboardName;
    }


    public Object[] getVal() {
        Object data[] = new Object[1];
        data[0] = BillboardID;
        return data;
    }

}
