package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import UserManagement.Replies.RegisterReply;
import UserManagement.Requests.RegisterRequest;
import User.UserPermissions;
import UserManagement.Requests.UserManagementRequest;
import UserManagement.Requests.UserManagementRequestType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
/**
 * @author Nikolai Taufao | N10481087
 */
public class RegisterUser implements ControlPanelComponent {
    public JTextField registerUsernameField;
    public JPasswordField registerPasswordField;
    public JCheckBox createBillboardsPerm;
    public JCheckBox editUsersPerm;
    public JCheckBox editBillboardsPerm;
    public JCheckBox scheduleBillboardsPerm;
    public JLabel registerReplyMessage;
    public JButton registerUserButton;
    public JPasswordField registerReenterPasswordField;

    /**
     * Create the register user panel which allows admins to register new users to the system.
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected RegisterUser(ControlPanelGUI controlPanelGUI) {
        setControlPanelComponents(controlPanelGUI);
        setRegisterButton();
    }


    private void setRegisterButton(){
        ActionListener buttonPress = e -> {
            try {
                registerUser();
            } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        };
        registerUserButton.addActionListener(buttonPress);
    }


    private void  registerUser() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        String username = registerUsernameField.getText();
        if (username.contains(" ")){
            String errorMessage = "Usernames cannot contain spaces.";
            JOptionPane.showMessageDialog(null, errorMessage);
        }

        String password = registerPasswordField.getText();
        String passwordReenter = registerReenterPasswordField.getText();
        boolean passwordsMatch = password.equals(passwordReenter);

        int minPasswordLength = 8;
        if (password.length() < minPasswordLength) {
            JOptionPane.showMessageDialog(null, "Passwords must be a least 8 characters long!");
        } else if (passwordsMatch){
            sendRegisterRequest(username, password);
        } else {
            JOptionPane.showMessageDialog(null,"Passwords do not match!");
        }
    }


    /**
     * Register a new user, consisting of their username, password and their permissions, to the system.
     * @param username the new user's username.
     * @param password the new user's password.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws ClassNotFoundException
     */
    private void sendRegisterRequest(String username, String password) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        // Get the selected permissions from this class's GUI permissions checkboxes.
        UserPermissions permissions = new UserPermissions(
                createBillboardsPerm.isSelected(),
                editBillboardsPerm.isSelected(),
                scheduleBillboardsPerm.isSelected(),
                editUsersPerm.isSelected()
        );
        RegisterRequest request = new RegisterRequest(username, password, permissions);
        UserManagementRequest registerRequest = new UserManagementRequest(UserManagementRequestType.register, request);

        RegisterReply registerReply = (RegisterReply) registerRequest.getOIS().readObject();
        registerRequest.closeConnection();

        if (registerReply.isSuccess()){
            String registerIsSuccessMessage = "User '"+username+"' successfully registered!";
            JOptionPane.showMessageDialog(null, registerIsSuccessMessage);
        } else {
            String errorMessage = registerReply.getErrorMessage();
            JOptionPane.showMessageDialog(null, errorMessage);
        }
    }
    
    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.editUsersPerm = controlPanelGUI.editUsersPerm;
        this.editBillboardsPerm = controlPanelGUI.editBillboardsPerm;
        this.registerUsernameField = controlPanelGUI.registerUsernameField;
        this.registerPasswordField = controlPanelGUI.registerPasswordField;
        this.registerUserButton = controlPanelGUI.registerUserButton;
        this.registerReenterPasswordField = controlPanelGUI.registerReenterPasswordField;
        this.createBillboardsPerm = controlPanelGUI.createBillboardsPerm;
        this.scheduleBillboardsPerm = controlPanelGUI.scheduleBillboardsPerm;
        this.registerReplyMessage = controlPanelGUI.registerReplyMessage;
    }
}
