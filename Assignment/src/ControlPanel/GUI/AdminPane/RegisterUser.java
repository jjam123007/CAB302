package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelGUI;
import UserManagement.ClientUser;
import UserManagement.RegisterReply;
import UserManagement.RegisterRequest;
import UserManagement.UserPermissions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class RegisterUser extends ControlPanelGUI {
    public RegisterUser(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        super(controlPanelGUI);
        setRegisterButton();
    }

    private void setRegisterButton(){
            ActionListener buttonPress = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        register();
                    } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }
            };
            registerUserButton.addActionListener(buttonPress);
    }

    private void register() throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        String registerIsSuccessMessage = "User successfully registered!";

        UserPermissions permissions = new UserPermissions(
                createBillboardsPerm.isSelected(),
                editBillboardsPerm.isSelected(),
                scheduleBillboardsPerm.isSelected(),
                editUsersPerm.isSelected()
        );
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();

        RegisterRequest registerRequest= new RegisterRequest(username, password, permissions, ClientUser.getToken());
        oos.writeObject(registerRequest);
        oos.flush();
        RegisterReply registerReply = (RegisterReply) ois.readObject();

        if (registerReply.isSuccess()){
            registerReplyMessage.setText(registerIsSuccessMessage);
        } else {
            registerReplyMessage.setEnabled(true);
            registerReplyMessage.setText(registerReply.getErrorMessage());
        }
    }
}
