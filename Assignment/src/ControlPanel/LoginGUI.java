package ControlPanel;

import ControlPanel.GUI.ControlPanelGUI;
import UserManagement.LoginReply;
import UserManagement.LoginRequest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

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

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public LoginGUI() throws IOException {
        this.frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(loginPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        createStreams();
        setLoginButton();
    }

    private void createStreams() throws IOException {
        this.socket = new Socket("localhost", 3310);
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
            ControlPanelGUI controlPanel = new ControlPanelGUI(socket,oos,ois);
            this.frame.dispose();
            System.out.println("hello");
        }else{
            messageLabel.setText(loginReply.getErrorMessage());
        }
    }


}
