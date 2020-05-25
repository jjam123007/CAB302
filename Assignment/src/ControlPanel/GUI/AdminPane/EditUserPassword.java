package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import UserManagement.Replies.ChangeUserPasswordReply;
import UserManagement.Requests.EditUserPropertyRequest;
import UserManagement.Requests.UserManagementRequest;
import UserManagement.Requests.UserManagementRequestType;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

public class EditUserPassword implements ControlPanelComponent {
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    public JPasswordField editReenterPasswordField;
    public JPasswordField editPasswordField;
    public JButton changePasswordButton;
    private String selectedUser = null;
    protected void setSelectedUser(String selectedUser){
        this.selectedUser = selectedUser;
    }

    public EditUserPassword(ControlPanelGUI controlPanelGUI){
        setControlPanelComponents(controlPanelGUI);
        setChangePasswordButton();
    }

    private void setChangePasswordButton() {
        ActionListener changePasswordButtonAction = e -> {
            try {
                String password = editPasswordField.getText();
                if (canChangePasswords(password, editReenterPasswordField.getText()))
                {
                    changePassword(selectedUser, password, oos , ois);
                }
            } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
            }
        };
        changePasswordButton.addActionListener(changePasswordButtonAction);
    }

    public static boolean canChangePasswords(String password, String passwordReenter) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        int minPasswordLength = 8;
        if (password.length() < minPasswordLength){
            JOptionPane.showMessageDialog(null, "Passwords must be a least 8 characters long!");
        } else if (password.equals(passwordReenter)){
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Passwords do not match!");
        }
        return false;
    }

    public static void changePassword(String username, String password, ObjectOutputStream oos, ObjectInputStream ois) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        EditUserPropertyRequest editUserPropertyRequest = new EditUserPropertyRequest(username, password);
        UserManagementRequest userManagementRequest = new UserManagementRequest(UserManagementRequestType.changePassword, editUserPropertyRequest);
        oos.writeObject(userManagementRequest);
        ChangeUserPasswordReply changeUserPasswordReply = (ChangeUserPasswordReply) ois.readObject();
        if (changeUserPasswordReply.isSuccess()){
            String successMessage = "Password successfully changed!";
            JOptionPane.showMessageDialog(null, successMessage);
        }else{
            JOptionPane.showMessageDialog(null, changeUserPasswordReply.getErrorMessage());
        }
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.oos = controlPanelGUI.oos;
        this.ois = controlPanelGUI.ois;

        this.editReenterPasswordField = controlPanelGUI.editReenterPasswordField;
        this.editPasswordField = controlPanelGUI.editPasswordField;
        this.changePasswordButton = controlPanelGUI.changePasswordButton;
    }
}
