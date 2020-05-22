package UserManagement;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws SQLException, IOException, NoSuchAlgorithmException, ClassNotFoundException {
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
        LoginReply loginReply = (LoginReply) ois.readObject();
        oos.flush();
        if (loginReply.isSuccess()){
            String tokenString = loginReply.getSessionToken();
            new ClientToken(tokenString);
            System.out.println("User login successful!");
            System.out.println("Session token: "+ ClientToken.getToken());
        }else{
            System.out.println(loginReply.getErrorMessage());
        }

        System.out.println("Please enter a username");
        String username2 = userInput.nextLine();
        System.out.println("Please enter a password");
        String password2 = userInput.nextLine();

        UserPermissions permissions = new UserPermissions(true,false,false,true);
        System.out.println(ClientToken.getToken());
        RegisterRequest registerRequest= new RegisterRequest(username2, password2, permissions, ClientToken.getToken());
        oos.writeObject(registerRequest);
        oos.flush();
        RegisterReply registerReply = (RegisterReply) ois.readObject();
        if (registerReply.isSuccess()){
            System.out.println("Success!");
        }
    }
}
