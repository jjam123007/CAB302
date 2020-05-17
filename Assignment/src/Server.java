
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.*;
import java.sql.Statement;


public class Server {

    private Connection connection;

    public static void main (String [] args) throws IOException, ClassNotFoundException, SQLException {
        ServerSocket serverSocket = new ServerSocket(3310);
        Socket socket = serverSocket.accept();
        System.out.println("Connected to "+ socket.getInetAddress());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream( socket.getInputStream());

        for(;;){
            try {
                //MyClass o = (MyClass) ois.readObject();
                //Object[] requestType = o.getVal();
                String requestType = ois.readUTF();
                System.out.println("Request type :"+requestType);
                switch(requestType){
                    case "addBillboard" : {
                        Add o2 = (Add) ois.readObject();
                        Object[]  data = o2.getVal();
                        System.out.println("Name :"+data[0]);
                        System.out.println("Msg :"+data[1]);
                        System.out.println("Info :"+data[2]);
                        System.out.println("Url :"+data[3]);

                        Statement statement = DBConnection.getInstance().createStatement();
                        statement.executeQuery("insert into billboard values(null,'"+data[0]+"','"+data[1]+"','"+data[2]+"','"+data[3]+"');");
                        statement.executeQuery("insert into view (BillboardName, message, info, url) values('"+data[0]+"','"+data[1]+"','"+data[2]+"','"+data[3]+"');");
                        statement.close();


                        break;
                    }

                    case "showTable" : {
                        Object [][] data;

                        Statement statement = DBConnection.getInstance().createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM view");

                        int rowcount = 0;

                        if (resultSet.last()) {
                            rowcount = resultSet.getRow();
                            resultSet.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
                        }
                        data =  new Object[rowcount][8];

                        for (int i=0; i<rowcount; i++){
                            resultSet.next();
                            String billboardID = Integer.toString(resultSet.getInt(1));
                            String BillboardName = resultSet.getString(2);
                            String info = resultSet.getString(3);
                            String msg = resultSet.getString(4);
                            String url = resultSet.getString(5);
                            String schduledDate = resultSet.getString(6);
                            Time startTime = resultSet.getTime(7);
                            Time endTime =  resultSet.getTime(8);
                            Object[] myString= {billboardID,BillboardName,info,msg,url,schduledDate,startTime,endTime};
                            data[i]=myString;
                        }
                        statement.close();
                        dataArray tableData = new dataArray(data);
                        oos.writeObject(tableData);
                        oos.flush();
                        break;
                    }

                    case "delete" : {
                        Delete deleteData = (Delete) ois.readObject();
                        Object[] data = deleteData.getVal();
                        System.out.println("ID :"+data[0]);
                        Statement statement = DBConnection.getInstance().createStatement();
                        statement.executeQuery("delete from view where billboardID=("+ data[0]+");");
                        statement.close();
                        break;
                    }
                    case "editTable" : {
                        Edit editData = (Edit) ois.readObject();
                        Object[] data = editData.getVal();
                        System.out.println("ID to edit :"+data[0]);
                        Statement statement = DBConnection.getInstance().createStatement();
                        System.out.println("update billboard set BillboardName="+data[1]+", message="+ data[2]+",info="+ data[3]+",url="+ data[4]+" where billboardID="+ data[0]+";");
                        statement.executeQuery("update billboard set BillboardName='"+data[1]+"', message='"+ data[2]+"',info='"+ data[3]+"',url='"+ data[4]+"' where billboardID='"+ data[0]+"';");
                        statement.close();
                        break;
                    }


                }

            } catch (EOFException e) {
                e.printStackTrace();
            }





        }
        // oos.close();
        //ois.close();
        //socket.close();

    }
}





