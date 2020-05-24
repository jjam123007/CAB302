package ControlPanel.GUI;

import ControlPanel.GUI.ControlPanelGUI;
import User.ClientUser;
import User.LoginReply;
import User.LoginRequest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginGUI {

    private JFrame frame;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel loginLabel;

    private JPanel loginPanel;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private JLabel messageLabel;
    private JButton loginButton;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;
    public LoginGUI() throws IOException {
        this.frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(loginPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        setLoginButton();
        setStreams();
    }

    private void setStreams() throws IOException {
        socket = new Socket("localhost",3310);
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        System.out.println("Connected to "+ socket.getInetAddress());
        oos = new ObjectOutputStream(os);
        ois = new ObjectInputStream(is);
    }

    private void setLoginButton(){
        ActionListener buttonPress = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { sendLoginRequest(); } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException | SQLException exception) { exception.printStackTrace(); }
            }
        };
        loginButton.addActionListener(buttonPress);
    }

    private void sendLoginRequest() throws IOException, NoSuchAlgorithmException, ClassNotFoundException, SQLException {
        String username = "test";//usernameField.getText();
        String password = "password";//passwordField.getText();
        LoginRequest login = new LoginRequest(username, password);
        oos.writeObject(login);
        oos.flush();
        handleLoginReply();
    }

    private void handleLoginReply() throws IOException, ClassNotFoundException, SQLException {
        LoginReply loginReply = (LoginReply) ois.readObject();
        if (loginReply.isSuccess()){
            new ClientUser(loginReply.getSessionToken(), loginReply.getPermissions());
            new ControlPanelGUI(socket,oos,ois);
            System.out.println(loginReply.getUsername());;
            this.frame.dispose();

            System.out.println("login Success");
        }else{
            messageLabel.setText(loginReply.getErrorMessage());
        }
    }


}
