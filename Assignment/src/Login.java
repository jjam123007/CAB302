import Billboard.Billboards;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class Login {

    public  static void main (String [] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {

        Socket socket = new Socket("localhost", 3310);

        OutputStream os = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(inputStream);

        Scanner userInput = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Please enter your username");
        String username = userInput.nextLine();
        System.out.println("Please enter your password");
        String password = userInput.nextLine();

        LoginRequest login = new LoginRequest(username, password);
        oos.writeObject(login);
        oos.flush();
        LoginReply loginReply = (LoginReply) ois.readObject();
        if (loginReply.Success()){
            System.out.println("Login Successful");
        }else{
            System.out.println(loginReply.getErrorMessage());
        }



        //ois.close();
        //oos.close();
        //socket.close();
    }
}
