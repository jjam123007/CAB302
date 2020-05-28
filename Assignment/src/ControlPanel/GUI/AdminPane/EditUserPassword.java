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
/**
 * @author Nikolai Taufao | N10481087
 */
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

    /**
     * Create the edit users tab that allows system admins to delete users or change their passwords and permissions.
     * @param controlPanelGUI
     */
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

    /**
     * Check if the two provided passwords match and have a minimal length of 8 characters.
     * @param password
     * @param passwordReenter
     * @return Return true if the two provided passwords match and have a length of at least 8 characters.
     * Else, return false and show a dialog box containing an error message.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static boolean canChangePasswords(String password, String passwordReenter) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        int minPasswordLength = 8;
        boolean passwordsMatch = password.equals(passwordReenter);

        if (password.length() < minPasswordLength){
            JOptionPane.showMessageDialog(null, "Passwords must be a least 8 characters long!");
        } else if (passwordsMatch){
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Passwords do not match!");
        }
        return false;
    }


    /**
     * Send an edit user property request to the server that changes a target user's password to a new one specified in the method header.
     * @param username the target user.
     * @param password the new password.
     * @param oos the object output stream of the client.
     * @param ois the object input stream of the client.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws ClassNotFoundException
     */
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
