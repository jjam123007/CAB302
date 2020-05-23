package ControlPanel;

import java.io.Serializable;

public class SerializeArray implements Serializable {
    private Object [][] data;

    public SerializeArray(Object [][] data){
        this.data = data;
    }

    public Object[][] getData() {
        return data;
    }

}


