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
                checkPassword();
            } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
            }
        };
        changePasswordButton.addActionListener(changePasswordButtonAction);
    }

    protected void checkPassword() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        String password = editPasswordField.getText();
        String confirmPassword = editReenterPasswordField.getText();
        if (password.equals(confirmPassword)){
            changePassword(selectedUser, password);
        } else{
            String errorMessage = "Passwords do not match!";
            JOptionPane.showMessageDialog(null, errorMessage);
        }
    }

    private void changePassword(String username, String password) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
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
