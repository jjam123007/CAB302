package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import User.UserPermissions;

import javax.swing.*;

public class EditUserPermissions implements ControlPanelComponent {

    protected String selectedUser = null;
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

    public JPasswordField editReenterPasswordField;
    public JPasswordField editPasswordField;
    public JButton changePasswordButton;

    public EditUserPermissions(ControlPanelGUI controlPanelGUI){
        setControlPanelComponents(controlPanelGUI);
    }


    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
         this.editUsersAdminPermEdit = controlPanelGUI.editUsersAdminPermEdit;
         this.createBillboardsPermEdit = controlPanelGUI.createBillboardsPermEdit;
         this.scheduleBillboardsPermEdit = controlPanelGUI.scheduleBillboardsPermEdit;
         this.editBillboardsPermEdit = controlPanelGUI.editBillboardsPermEdit;

         this.editReenterPasswordField = controlPanelGUI.editReenterPasswordField;
         this.editPasswordField = controlPanelGUI.editPasswordField;
         this.changePasswordButton = controlPanelGUI.changePasswordButton;
    }
}
