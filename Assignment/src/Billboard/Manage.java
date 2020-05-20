package Billboard;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;

public class Manage implements Serializable {
    private final int id;
    private final String name;
    private final String message;
    private final String info;
    private final String url;


    public Manage (int id, String name, String message, String info, String url){
        this.id = id;
        this.name = name;
        this.message = message;
        this.info = info;
        this.url = url;
    }

    public void Edit() throws SQLException {
        System.out.println("ID to edit :"+id);
        Statement statement = DBConnection.getInstance().createStatement();
        //System.out.println("update billboard set BillboardName="name", message="message"+ data[2]+",info="+ data[3]+",url="+ data[4]+" where billboardID="+ data[0]+";");
        statement.executeQuery("update billboard set BillboardName='"+ name+"', message='"+ message+"',info='"+ info+"',url='"+ url+"' where billboardID='"+ id+"';");
        statement.close();
    }

}