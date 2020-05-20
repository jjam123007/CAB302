
import Billboard.Add;
import Billboard.Delete;
import Billboard.Edit;


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
                Object requestObject = (Object) ois.readObject(); System.out.println("Request type :"+ requestObject);

                if (requestObject instanceof BillboardRequest)
                {
                    String request = ((BillboardRequest) requestObject).request;
                    String token = ((BillboardRequest) requestObject).token;
                    Object[] data = ((BillboardRequest) requestObject).data;
                    switch (request) {
                        case "addBillboard": {
                            //Add o2 = (Add) ois.readObject();
                            //Object[] data1 = o2.getVal();
                            System.out.println("Name :" + data[0]);
                            System.out.println("Msg :" + data[1]);
                            System.out.println("Info :" + data[2]);
                            System.out.println("Url :" + data[3]);

                            Statement statement = DBConnection.getInstance().createStatement();
                            statement.executeQuery("insert into billboard values(null,'" + data[0] + "','" + data[1] + "','" + data[2] + "','" + data[3] + "');");
                            statement.executeQuery("insert into view (BillboardName, message, info, url) values('" + data[0] + "','" + data[1] + "','" + data[2] + "','" + data[3] + "');");
                            statement.close();


                            break;
                        }
                        case "addView": {
                            //Add o3 = (Add) ois.readObject();
                            //Object[] data = o3.getVal();
                            System.out.println("billboardId :" + data[0]);
                            System.out.println("scheduledDate :" + data[1]);
                            System.out.println("startTime :" + data[2]);
                            System.out.println("endTime :" + data[3]);

                            Statement statement = DBConnection.getInstance().createStatement();
                            statement.executeQuery("update view set scheduleddate='" + data[1] + "', starttime='" + data[2] + "', endtime='" + data[3] + "'Where billboardId='" + data[0] + "'");
                            statement.close();


                            break;
                        }

                        case "showTable": {
                            Object[][] tableData;

                            Statement statement = DBConnection.getInstance().createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM view");

                            int rowcount = 0;

                            if (resultSet.last()) {
                                rowcount = resultSet.getRow();
                                resultSet.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
                            }
                            tableData = new Object[rowcount][8];

                            for (int i = 0; i < rowcount; i++) {
                                resultSet.next();
                                String billboardID = Integer.toString(resultSet.getInt(1));
                                String BillboardName = resultSet.getString(2);
                                String info = resultSet.getString(3);
                                String msg = resultSet.getString(4);
                                String url = resultSet.getString(5);
                                String schduledDate = resultSet.getString(6);
                                Time startTime = resultSet.getTime(7);
                                Time endTime = resultSet.getTime(8);
                                Object[] myString = {billboardID, BillboardName, info, msg, url, schduledDate, startTime, endTime};
                                tableData[i] = myString;
                            }
                            statement.close();
                            SerialDataArray d2= new SerialDataArray(tableData);
                            oos.writeObject(d2);
                            oos.flush();
                            break;
                        }

                        case "delete": {
                            //Delete deleteData = (Delete) ois.readObject();
                            //Object[] data = deleteData.getVal();
                            System.out.println("ID :" + data[0]);
                            Statement statement = DBConnection.getInstance().createStatement();
                            statement.executeQuery("delete from view where billboardID=(" + data[0] + ");");
                            statement.close();
                            break;
                        }
                        case "editTable": {

                            break;
                        }


                    }
                }

            } catch (EOFException e) {
                e.printStackTrace();
            }




        }
    }
}





