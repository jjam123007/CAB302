import java.io.Serializable;
import java.util.Arrays;

public class Edit implements Serializable {
    private final int id;
    private final String query1;
    private final String query2;
    private final String query3;
    private final String query4;


    public Edit (int id, String query1, String query2, String query3, String query4){
        this.id = id;
        this.query1 = query1;
        this.query2 = query2;
        this.query3 = query3;
        this.query4 = query4;

    }

    public Object[] getVal() {
        Object data[] = new Object[5];
        data[0] = id;
        data[1] = query1;
        data[2] = query2;
        data[3] = query3;
        data[4] = query4;
        return data;
    }

}