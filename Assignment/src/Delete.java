import java.io.Serializable;

public class Delete implements Serializable {
    private final String BillboardName;


    public Delete (String BillboardName){
        this.BillboardName = BillboardName;
    }


    public Object[] getVal() {
        Object data[] = new Object[1];
        data[0] = BillboardName;
        return data;
    }

}
