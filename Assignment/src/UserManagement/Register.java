package UserManagement;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Register {
    public static void main (String [] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 3310);

        OutputStream os = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(inputStream);

        Scanner userInput = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Please enter a username");
        String username = userInput.nextLine();
        System.out.println("Please enter a password");
        String password = userInput.nextLine();

        UserPermissions permissions = new UserPermissions(true,false,false,true);
        System.out.println(ClientToken.getToken());
        RegisterRequest registerRequest= new RegisterRequest(username, password, permissions, ClientToken.getToken());
        oos.writeObject(registerRequest);
        oos.flush();
        RegisterReply registerReply = (RegisterReply) ois.readObject();
        if (registerReply.isSuccess()){
            System.out.println("Success!");
        }


    }
}
