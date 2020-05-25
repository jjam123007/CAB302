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

public class AccountPane implements ControlPanelComponent {
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    public JFrame frame;
    public JButton clientChangePasswordButton;
    public  JPasswordField clientReenterPasswordField;
    public  JPasswordField clientNewPasswordField;
    public JButton logoutButton;

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

    private void logout() throws IOException, ClassNotFoundException {
        UserManagementRequest logoutRequest = new UserManagementRequest(UserManagementRequestType.logout);
        oos.writeObject(logoutRequest);
        LogoutReply logoutReply = (LogoutReply) ois.readObject();
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
        ActionListener changePasswordButtonAction = e -> {
            try {
                String password = clientNewPasswordField.getText();
                if (EditUserPassword.canChangePasswords(password, clientReenterPasswordField.getText())){
                    EditUserPassword.changePassword(ClientUser.getUsername(), password, oos, ois);
                }
            } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
            }
        };
        clientChangePasswordButton.addActionListener(changePasswordButtonAction);
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.oos = controlPanelGUI.oos;
        this.ois = controlPanelGUI.ois;

        this.frame = controlPanelGUI.frame;
        this.logoutButton = controlPanelGUI.logoutButton;
        this.clientNewPasswordField = controlPanelGUI.clientNewPasswordField;
        this.clientReenterPasswordField = controlPanelGUI.clientReenterPasswordField;
        this.clientChangePasswordButton = controlPanelGUI.clientChangePasswordButton;
    }
}
