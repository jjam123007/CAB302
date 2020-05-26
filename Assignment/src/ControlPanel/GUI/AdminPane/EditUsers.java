package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import UserManagement.Replies.RemoveUserReply;
import UserManagement.Replies.ViewUserPermissionsReply;
import UserManagement.Requests.UserManagementRequest;
import UserManagement.Requests.UserManagementRequestType;
import UserManagement.Replies.ViewUsersReply;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

public class EditUsers implements ControlPanelComponent {
    private JTable usersTable;
    protected String selectedUser = null;

    public JButton updateUserListButton;
    public JButton removeUserButton;
    public String[] usernameColumn = {"Username"};
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    ListSelectionListener userSelection;


    public EditUsers(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException, SQLException {
        setControlPanelComponents(controlPanelGUI);
        EditUserPermissions editUserPermissions = new EditUserPermissions(controlPanelGUI);
        EditUserPassword editUserPassword = new EditUserPassword(controlPanelGUI);

        updateUsersTable();
        setUpdateUserListButton();
        setRemoveUserButton();
        setUserSelection(editUserPermissions, editUserPassword);
    }

    private void setRemoveUserButton(){
        String confirmMessage = "Are you sure that you want to remove user '"+selectedUser+"'?";
        ActionListener removeUserButtonAction = e -> {
            try {
                int confirm = JOptionPane.showConfirmDialog(null, confirmMessage);
                if (confirm == JOptionPane.YES_OPTION)
                {
                    removeUser();
                    updateUsersTable();
                }
            } catch (IOException | ClassNotFoundException | SQLException ioException) {
                ioException.printStackTrace();
            }
        };

        removeUserButton.addActionListener(removeUserButtonAction);
    }
    private void setUpdateUserListButton(){
        ActionListener updateUserList = e -> {
            try {
                updateUsersTable();
            } catch (SQLException | IOException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        };
        updateUserListButton.addActionListener(updateUserList);
    }
    private void setUserSelection(EditUserPermissions editUserPermissions, EditUserPassword editUserPassword){
        userSelection = e -> {
            selectedUser = usersTable.getValueAt(usersTable.getSelectedRow(), 0).toString();
            try {
                getUserPermissions(selectedUser, editUserPermissions);
                editUserPermissions.setSelectedUser(selectedUser);
                editUserPassword.setSelectedUser(selectedUser);
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        };

        usersTable.getSelectionModel().addListSelectionListener(userSelection);
    }
    private void getUserPermissions(String username, EditUserPermissions editUserPermissions) throws IOException, ClassNotFoundException {
        UserManagementRequest getUserPermissionsRequest =  new UserManagementRequest(UserManagementRequestType.getPermissions, username);
        oos.writeObject(getUserPermissionsRequest);
        ViewUserPermissionsReply viewUserPermissionsReply = (ViewUserPermissionsReply) ois.readObject();
        if (viewUserPermissionsReply.isSuccess()){
            editUserPermissions.setUserPermissions(viewUserPermissionsReply.getUserPermissions());
        } else{
            JOptionPane.showMessageDialog(null, viewUserPermissionsReply.getErrorMessage());
        }
    }
    private void removeUser() throws IOException, ClassNotFoundException {
        System.out.println("Fail1");

        UserManagementRequest removeUserRequest = new UserManagementRequest(UserManagementRequestType.remove, selectedUser);
        System.out.println("Fail2");
        oos.writeObject(removeUserRequest);
        RemoveUserReply removeUserReply = (RemoveUserReply) ois.readObject();
        if (removeUserReply.isSuccess()){
            String successMessage = "User '"+selectedUser+"' successfully removed";
            JOptionPane.showMessageDialog(null, successMessage);
        } else{
            JOptionPane.showMessageDialog(null, removeUserReply.getErrorMessage());
        }
        System.out.println("Fail2");
    }
    private void updateUsersTable() throws SQLException, IOException, ClassNotFoundException {
        usersTable.getSelectionModel().removeListSelectionListener(userSelection);
        UserManagementRequest viewUsersRequest = new UserManagementRequest(UserManagementRequestType.getUsernames);
        oos.writeObject(viewUsersRequest);
        ViewUsersReply viewUsersReply = (ViewUsersReply) ois.readObject();

        if (viewUsersReply.isSuccess()){
            Object[][] usernames = listToTableData(viewUsersReply.getUserDataTable());
            DefaultTableModel usersModel = new DefaultTableModel(usernames, usernameColumn);
            usersTable.setModel(usersModel);
            usersTable.setColumnSelectionAllowed(false);
        } else{
            JOptionPane.showMessageDialog(null, viewUsersReply.getErrorMessage());
        }
        usersTable.getSelectionModel().addListSelectionListener(userSelection);

    }

    public Object[][] listToTableData(List<Object> list){
        Object[] array =  list.toArray(new Object[0]);
        System.out.println(array.length);
        Object[][] dataArray = new Object[array.length][1];

        for(int dataIndex = 0; dataIndex < array.length; dataIndex++){
            System.out.println(dataIndex);
            dataArray[dataIndex][0] = array[dataIndex];
        }
        return  dataArray;
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.removeUserButton = controlPanelGUI.removeUserButton;
        this.usersTable = controlPanelGUI.usersTable;
        this.updateUserListButton = controlPanelGUI.updateUserListButton;
        this.oos = controlPanelGUI.oos;
        this.ois = controlPanelGUI.ois;
    }
}
