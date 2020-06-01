package ControlPanel.GUI;

import Database.DBConnection;
import User.ClientUser;
import UserManagement.DataSecurity;
import UserManagement.Replies.LoginReply;
import UserManagement.Replies.RegisterReply;
import UserManagement.Requests.LoginRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Nikolai Taufao | N10481087
 */
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

    /**
     * Create the login form which appears on control panel startup.
     * @throws IOException
     */
    public LoginGUI() throws IOException {
        this.frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(loginPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        setLoginButton();
    }

    private void setLoginButton(){
        ActionListener login = e -> {
            try { sendLoginRequest(); }
            catch (NoSuchAlgorithmException | IOException | ClassNotFoundException | SQLException exception)
            { exception.printStackTrace(); }
        };
        loginButton.addActionListener(login);
    }

    private void sendLoginRequest() throws IOException, NoSuchAlgorithmException, ClassNotFoundException, SQLException {


        String username = "admin";//usernameField.getText();
        String password = "12345678";//passwordField.getText();
        LoginRequest login = new LoginRequest(username, password);
        try{
            handleLoginReply(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLoginReply(LoginRequest login) throws IOException, ClassNotFoundException, SQLException {
        LoginReply loginReply = (LoginReply) login.getOIS().readObject();
        login.closeConnection();
        if (loginReply.isSuccess()){
            // Store the client user's session token, username and permissions in memory.
            new ClientUser(loginReply.getSessionToken(), loginReply.getUsername(), loginReply.getPermissions());
            new ControlPanelGUI();
            this.frame.dispose();
            System.out.println("login Success");
        }else{
            messageLabel.setText(loginReply.getErrorMessage());
            frame.pack();
            messageLabel.setForeground(Color.red);
        }
    }


}
