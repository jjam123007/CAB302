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
/**
 * @author Nikolai Taufao | N10481087
 */
public class EditUsers implements ControlPanelComponent {
    // THe table containing all of the usernames registered in the system.
    private JTable usersTable;
    // The current user selected in the users table.
    protected String selectedUser = null;

    public JButton updateUserListButton;
    public JButton removeUserButton;
    public String[] usernameColumn = {"Username"};
    ListSelectionListener userSelection;


    /**
     * Create the edit users panel which allows admins to change the permissions and passwords of a user selected in a JTable.
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
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
                    //Remove the user selected from the table.
                    removeUser();
                    //Refresh the users table to show the change.
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
        //Set the user selection of the users JTable.
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

    /**
     * Request the permissions of a selected user from the server and pass this information to the edit user permissions section.
     * @param username the selected user.
     * @param editUserPermissions the edit user permissions section of the edit users panel.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void getUserPermissions(String username, EditUserPermissions editUserPermissions) throws IOException, ClassNotFoundException {
        UserManagementRequest getUserPermissionsRequest =  new UserManagementRequest(UserManagementRequestType.getPermissions, username);
        //Get the server reply.
        ViewUserPermissionsReply viewUserPermissionsReply = (ViewUserPermissionsReply) getUserPermissionsRequest.getOIS().readObject();
        getUserPermissionsRequest.closeConnection();

        if (viewUserPermissionsReply.isSuccess()){
            // Pass the user permissions to the edit user permissions section.
            editUserPermissions.setUserPermissions(viewUserPermissionsReply.getUserPermissions());
        } else{
            JOptionPane.showMessageDialog(null, viewUserPermissionsReply.getErrorMessage());
        }
    }
    private void removeUser() throws IOException, ClassNotFoundException {
        // Remove the user that was selected by the admin in the users JTable.
        UserManagementRequest removeUserRequest = new UserManagementRequest(UserManagementRequestType.remove, selectedUser);
        //Get the server reply.
        RemoveUserReply removeUserReply = (RemoveUserReply) removeUserRequest.getOIS().readObject();
        removeUserRequest.closeConnection();

        if (removeUserReply.isSuccess()){
            String successMessage = "User '"+selectedUser+"' successfully removed";
            JOptionPane.showMessageDialog(null, successMessage);
        } else{
            JOptionPane.showMessageDialog(null, removeUserReply.getErrorMessage());
        }
    }


    /**
     * Retrieve the system usernames from the server and display them in a JTable.
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void updateUsersTable() throws SQLException, IOException, ClassNotFoundException {
        usersTable.getSelectionModel().removeListSelectionListener(userSelection);
        usersTable.setDefaultEditor(Object.class, null);
        UserManagementRequest viewUsersRequest = new UserManagementRequest(UserManagementRequestType.getUsernames);
        //Get the server reply.
        ViewUsersReply viewUsersReply = (ViewUsersReply) viewUsersRequest.getOIS().readObject();
        viewUsersRequest.closeConnection();

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

    /**
     * Convert a list to an object array that can be used as the data parameter for a JTable.
     * @param list
     * @return a 2D object array.
     */
    public Object[][] listToTableData(List<Object> list){
        Object[] array =  list.toArray(new Object[0]);
        Object[][] dataArray = new Object[array.length][1];

        for(int dataIndex = 0; dataIndex < array.length; dataIndex++){
            dataArray[dataIndex][0] = array[dataIndex];
        }
        return dataArray;
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.removeUserButton = controlPanelGUI.removeUserButton;
        this.usersTable = controlPanelGUI.usersTable;
        this.updateUserListButton = controlPanelGUI.updateUserListButton;
    }
}
