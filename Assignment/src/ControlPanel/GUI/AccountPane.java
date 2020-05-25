package ControlPanel.GUI;

import ControlPanel.GUI.AdminPane.EditUserPassword;
import User.ClientUser;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

public class AccountPane implements ControlPanelComponent {
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    public JButton clientChangePasswordButton;
    public  JPasswordField clientReenterPasswordField;
    public  JPasswordField clientNewPasswordField;

    public AccountPane(ControlPanelGUI controlPanelGUI){
        setControlPanelComponents(controlPanelGUI);
        setChangePasswordPane();
    }

    private void setChangePasswordPane() {
        setChangePasswordButton();
    }

    private void setChangePasswordButton() {
        ActionListener changePasswordButtonAction = e -> {
            try {
                checkPassword();
            } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
            }
        };
        clientChangePasswordButton.addActionListener(changePasswordButtonAction);
    }

    protected void checkPassword() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        String password = clientNewPasswordField.getText();
        String confirmPassword = clientReenterPasswordField.getText();
        if (password.equals(confirmPassword)){
            EditUserPassword.changePassword(ClientUser.getUsername(), password, oos, ois);
        } else{
            String errorMessage = "Passwords do not match!";
            JOptionPane.showMessageDialog(null, errorMessage);
        }
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.oos = controlPanelGUI.oos;
        this.ois = controlPanelGUI.ois;

        this.clientNewPasswordField = controlPanelGUI.clientNewPasswordField;
        this.clientReenterPasswordField = controlPanelGUI.clientReenterPasswordField;
        this.clientChangePasswordButton = controlPanelGUI.clientChangePasswordButton;
    }
}
