import java.io.Serializable;
import java.util.Arrays;

public class SerialDataArray implements Serializable {
    private Object [][] data;

    public SerialDataArray(Object [][] data){
        this.data = data;
    }

    public Object[][] getData() {
        return data;
    }

}


