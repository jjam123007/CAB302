package ControlPanel.GUI;

import ControlPanel.GUI.AdminPane.EditUserPassword;
import User.ClientUser;
import UserManagement.Replies.LogoutReply;
import UserManagement.Requests.UserManagementRequest;
import UserManagement.Requests.UserManagementRequestType;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

/**
 * @author Nikolai Taufao | N10481087
 */
public class AccountPane implements ControlPanelComponent {
    public JFrame frame;
    public JButton clientChangePasswordButton;
    public  JPasswordField clientReenterPasswordField;
    public  JPasswordField clientNewPasswordField;
    public JButton logoutButton;

    /**
     * Create the account pane which allows client users to change their own password or logout.
     * @param controlPanelGUI
     */
    public AccountPane(ControlPanelGUI controlPanelGUI){
        setControlPanelComponents(controlPanelGUI);
        setChangePasswordPane();
        setLogoutButton();
    }

    private void setLogoutButton() {
        ActionListener logoutButtonAction = e -> {
            try {
                logout();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        };
        logoutButton.addActionListener(logoutButtonAction);
    }


    /**
     * Send a logout request to the server. This will log the user out of the client system and remove their session token from the server.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void logout() throws IOException, ClassNotFoundException {
        UserManagementRequest logoutRequest = new UserManagementRequest(UserManagementRequestType.logout);
        LogoutReply logoutReply = (LogoutReply) logoutRequest.getOIS().readObject();
        logoutRequest.closeConnection();

        if (logoutReply.isSuccess()){
            JOptionPane.showMessageDialog(null,"You have successfully logged out!");
            frame.dispose();
        } else{
            JOptionPane.showMessageDialog(null, logoutReply.getErrorMessage());
        };
    }

    private void setChangePasswordPane() {
        setChangePasswordButton();
    }

    private void setChangePasswordButton() {
        // Change the password of the current client user.
        ActionListener changePasswordButtonAction = e -> {
            try {
                String password = clientNewPasswordField.getText();
                if (EditUserPassword.canChangePasswords(password, clientReenterPasswordField.getText())){
                    EditUserPassword.changePassword(ClientUser.getUsername(), password);
                }
            } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
            }
        };
        clientChangePasswordButton.addActionListener(changePasswordButtonAction);
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {

        this.frame = controlPanelGUI.frame;
        this.logoutButton = controlPanelGUI.logoutButton;
        this.clientNewPasswordField = controlPanelGUI.clientNewPasswordField;
        this.clientReenterPasswordField = controlPanelGUI.clientReenterPasswordField;
        this.clientChangePasswordButton = controlPanelGUI.clientChangePasswordButton;
    }
}
