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
        Object[] data = o.getVal();

        System.out.println("ID :"+data[0]);
        System.out.println("Name :"+data[1]);
        System.out.println("Number :"+data[2]);

        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("insert into testtable values("+data[0]+",'"+data[1]+"',"+data[2]+")");
        statement.close();

        Statement statement2 = DBConnection.getInstance().createStatement();
        statement2.executeQuery("delete from testtable where personaId="+data[3]);
        statement2.close();
        //}

        oos.close();
        ois.close();
        socket.close();
    }


    public Server() throws SQLException, IOException {


        }
//        ServerSocket serverSocket = new ServerSocket(3306);
}





