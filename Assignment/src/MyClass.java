import java.io.Serializable;
import java.util.Arrays;

public class MyClass implements Serializable {
    private final int query1;
    private final String query2;
    private final float query3;
    private final int query4;


    public MyClass (int query1, String query2, float query3, int query4){
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
