import java.io.Serializable;
import java.util.Arrays;

public class Add implements Serializable {
    private final String query1;
    private final String query2;
    private final String query3;
    private final String query4;


    public Add (String query1, String query2, String query3, String query4){
        this.query1 = query1;
        this.query2 = query2;
        this.query3 = query3;
        this.query4 = query4;

    }

    public Object[] getVal() {
        Object data[] = new Object[4];
        data[0] = query1;
        data[1] = query2;
        data[2] = query3;
        data[3] = query4;

        return data;
    }

}
