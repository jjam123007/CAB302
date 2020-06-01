
package Billboard;



import java.io.Serializable;

/**
 * @author Haoze He(n10100351)
 * setter function to set value for BillboardReply elements and variables
 */
public class BillboardReply implements Serializable {
    private String message;

    public Object[][] getTableData() {
        return tableData;
    }

    private Object[][] tableData = null;

    public String getMessage() {return message;}

    public BillboardReply(String message){
        this.message = message;
    }
    public BillboardReply(Object[][] tableData){
        this.tableData = tableData;
    }
}