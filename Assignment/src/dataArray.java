import java.io.Serializable;
import java.util.Arrays;

public class dataArray implements Serializable {
    private Object [][] data;

    public dataArray (Object [][] data){
        this.data = data;
    }

    public Object[][] getData() {
        return data;
    }

}


