import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
       //Object o = "";

        //while ( !o.toString().contentEquals("quit") ) {
        MyClass o = (MyClass) ois.readObject();
        Object[] requestType = o.getVal();
        System.out.println("Request type :"+requestType[0]);

        if(requestType[0].toString().contentEquals("addBillboard")){
            System.out.println("HERE");
            Add o2 = (Add) ois.readObject();
            Object[]  data = o2.getVal();
            System.out.println("Name :"+data[0]);
            System.out.println("Msg :"+data[1]);
            System.out.println("Info :"+data[2]);
            System.out.println("Url :"+data[3]);


            Statement statement = DBConnection.getInstance().createStatement();
            statement.executeQuery("insert into billboard values('"+data[0]+"','"+data[1]+"','"+data[2]+"','"+data[3]+"');");
            statement.close();

            oos.close();
            ois.close();
            socket.close();
        } //else if


    }
    public Server() throws SQLException, IOException {


        }
//        ServerSocket serverSocket = new ServerSocket(3306);
}





