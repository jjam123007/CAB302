import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class Login {

    public  static void main (String [] args) throws IOException, NoSuchAlgorithmException {
        /*
        Socket socket = new Socket("localhost", 3310);

        OutputStream os = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        */
        Scanner userInput = new Scanner(System.in);  // Create a Scanner object

        //ame.contentEquals("quit") ) {
        System.out.println("Please enter your username");
        String userName = userInput.nextLine();

        System.out.println("Please enter your password");
        String password = userInput.nextLine();

        LoginRequest login = new LoginRequest(userName, password);
        /*
        oos.writeObject(login);
        ois.readUTF();
        oos.flush();


        ois.close();
        oos.close();
        socket.close();
        */
    }
}
