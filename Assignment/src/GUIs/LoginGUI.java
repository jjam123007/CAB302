package GUIs;

import UserManagement.ClientToken;
import UserManagement.LoginReply;
import UserManagement.LoginRequest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class LoginGUI {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel loginLabel;

    private JPanel loginPanel;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private JLabel messageLabel;
    private JButton loginButton;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JFrame frame;
    public LoginGUI() throws IOException {
        this.frame = new JFrame("Login");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setContentPane(loginPanel);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

        createStreams();
        setLoginButton();
    }

    private void createStreams() throws IOException {
        Socket socket = new Socket("localhost", 3310);
        OutputStream os = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        this.oos = new ObjectOutputStream(os);
        this.ois = new ObjectInputStream(inputStream);
    }

    private void setLoginButton(){
        ActionListener buttonPress = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { login(); } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException exception) { exception.printStackTrace(); }
            }
        };
        loginButton.addActionListener(buttonPress);
    }

    private void login() throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        LoginRequest login = new LoginRequest(username, password);
        oos.writeObject(login);
        oos.flush();
        LoginReply loginReply = (LoginReply) ois.readObject();
        if (loginReply.isSuccess()){
            //TODO: Create and display the client GUI after a successful login.
            createGUI();
            //this.frame.dispose();
        }else{
            messageLabel.setText(loginReply.getErrorMessage());
        }
    }

    private  static void createGUI() throws IOException, ClassNotFoundException {
        ClientGUI ui = new ClientGUI();
        JPanel root = ui.getRootPanel();
        JFrame frame = new JFrame();
        frame.setContentPane(root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
