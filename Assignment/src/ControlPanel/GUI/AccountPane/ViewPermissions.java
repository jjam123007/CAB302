package ControlPanel.GUI.AccountPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import User.ClientUser;
import User.UserPermissions;
import UserManagement.Replies.ViewUserPermissionsReply;
import UserManagement.Requests.UserManagementRequest;
import UserManagement.Requests.UserManagementRequestType;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;


public class ViewPermissions implements ControlPanelComponent {
    public JLabel accountUsernameLabel;
    public JButton updatePermissionsButton;
    public JCheckBox detailsCreateBillboardsPerm;
    public JCheckBox detailsEditBillboardsPerm;
    public JCheckBox detailsScheduleBillboardsPerm;
    public JCheckBox detailsEditUsersPerm;


    /**
     * Display the current control panel user's username and permissions under the 'Account Details' tab.
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ViewPermissions(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);

        //Show the client user's username.
        accountUsernameLabel.setText(ClientUser.getUsername());

        //Show the user's updated permissions.
        setUpdatePermissionsButton();

        showAccountPermissions();
    }

    private void setUpdatePermissionsButton() {

        ActionListener updatePermissions = e -> {
            try {
                showAccountPermissions();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        };
        updatePermissionsButton.addActionListener(updatePermissions);
    }


    //Retrieve the current control panel user's permissions from the server and display it.
    private void showAccountPermissions() throws IOException, ClassNotFoundException {
        UserManagementRequest getUserPermissionsRequest =  new UserManagementRequest(UserManagementRequestType.getPermissions, ClientUser.getUsername());
        //Get the server reply.
        ViewUserPermissionsReply viewUserPermissionsReply = (ViewUserPermissionsReply) getUserPermissionsRequest.getOIS().readObject();
        getUserPermissionsRequest.closeConnection();

        UserPermissions permissions = viewUserPermissionsReply.getUserPermissions();
        if (viewUserPermissionsReply.isSuccess())
        {
            //Set the checkboxes that match the user's permissions to selected.
            detailsCreateBillboardsPerm.setSelected(permissions.canCreateBillboards());
            detailsEditBillboardsPerm.setSelected(permissions.canEditBillboards());
            detailsScheduleBillboardsPerm.setSelected(permissions.canScheduleBillboards());
            detailsEditUsersPerm.setSelected(permissions.canEditUsers());
        } else {
            JOptionPane.showMessageDialog(null, viewUserPermissionsReply.getErrorMessage());
        }
    }


    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        accountUsernameLabel = controlPanelGUI.accountUsernameLabel;
        updatePermissionsButton = controlPanelGUI.updatePermissionsButton;
        detailsCreateBillboardsPerm = controlPanelGUI.detailsCreateBillboardsPerm;
        detailsEditBillboardsPerm = controlPanelGUI.detailsEditBillboardsPerm;
        detailsScheduleBillboardsPerm = controlPanelGUI.detailsScheduleBillboardsPerm;
        detailsEditUsersPerm = controlPanelGUI.detailsEditUsersPerm;
    }
}

