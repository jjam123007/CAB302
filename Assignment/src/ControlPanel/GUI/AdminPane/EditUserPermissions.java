package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import User.UserPermissions;
import UserManagement.Replies.EditUserPermisionsReply;
import UserManagement.Requests.EditUserPropertyRequest;
import UserManagement.Requests.UserManagementRequest;
import UserManagement.Requests.UserManagementRequestType;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EditUserPermissions implements ControlPanelComponent {

    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    private String selectedUser = null;
    protected void setSelectedUser(String selectedUser){
        this.selectedUser = selectedUser;
    }

    protected void setUserPermissions(UserPermissions userPermissions) {
        editUsersAdminPermEdit.setSelected(userPermissions.canEditUsers());
        createBillboardsPermEdit.setSelected(userPermissions.canCreateBillboards());
        scheduleBillboardsPermEdit.setSelected(userPermissions.canScheduleBillboards());
        editBillboardsPermEdit.setSelected(userPermissions.canEditBillboards());
    }

    public JCheckBox editUsersAdminPermEdit;
    public JCheckBox createBillboardsPermEdit;
    public JCheckBox scheduleBillboardsPermEdit;
    public JCheckBox editBillboardsPermEdit;

    public JButton changePermissionsButton;

    public EditUserPermissions(ControlPanelGUI controlPanelGUI){
        setControlPanelComponents(controlPanelGUI);
        setChangePermissionsButton();
    }

    private void setChangePermissionsButton(){
        ActionListener changePermissionButtonAction = e -> {
            try {
                changeUserPermissions();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        };
        changePermissionsButton.addActionListener(changePermissionButtonAction);
    }

    private void changeUserPermissions() throws IOException, ClassNotFoundException {
        boolean p1 = createBillboardsPermEdit.isSelected();
        boolean p2 = editBillboardsPermEdit.isSelected();
        boolean p3 = scheduleBillboardsPermEdit.isSelected();
        boolean p4 = editUsersAdminPermEdit.isSelected();

        UserPermissions userPermissions = new UserPermissions(p1,p2,p3,p4);
        EditUserPropertyRequest editUserPropertyRequest = new EditUserPropertyRequest(selectedUser, userPermissions);
        UserManagementRequest editUserPermissionsRequest = new UserManagementRequest(UserManagementRequestType.changePermissions, editUserPropertyRequest);
        oos.writeObject(editUserPermissionsRequest);
        EditUserPermisionsReply editUserPermisionsReply = (EditUserPermisionsReply) ois.readObject();

        if (editUserPermisionsReply.isSuccess()){
            String successMessage = "Permissions successfully changed for user '"+selectedUser+"'.";
            JOptionPane.showMessageDialog(null, successMessage);
        }else{
            JOptionPane.showMessageDialog(null, editUserPermisionsReply.getErrorMessage());
        }
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
         this.oos = controlPanelGUI.oos;
         this.ois = controlPanelGUI.ois;

         this.changePermissionsButton = controlPanelGUI.changePermissionsButton;
         this.editUsersAdminPermEdit = controlPanelGUI.editUsersAdminPermEdit;
         this.createBillboardsPermEdit = controlPanelGUI.createBillboardsPermEdit;
         this.scheduleBillboardsPermEdit = controlPanelGUI.scheduleBillboardsPermEdit;
         this.editBillboardsPermEdit = controlPanelGUI.editBillboardsPermEdit;

    }
}
