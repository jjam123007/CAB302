import java.io.Serializable;
import java.util.Arrays;

public class MyClass implements Serializable {
    private final String requestType;


    public MyClass (String requestType){
        this.requestType = requestType;
    }


    public Object[] getVal() {
        Object data[] = new Object[1];
        data[0] = requestType;
        return data;
    }

}
