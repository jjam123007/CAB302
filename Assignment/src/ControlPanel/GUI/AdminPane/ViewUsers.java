package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import UserManagement.Requests.UserManagementRequest;
import UserManagement.UserManagementRequestType;
import UserManagement.Replies.ViewUsersReply;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

public class ViewUsers implements ControlPanelComponent {
    private JTable usersTable;
    public JButton updateUserListButton;
    public JButton removeUserButton;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    String[] usernameColumn = {"Username"};

    public ViewUsers(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException, SQLException {
        setControlPanelComponents(controlPanelGUI);
        updateUsersTable();
        setUpdateUserListButton();
    }

    private void setUpdateUserListButton(){
        ActionListener updateUserList = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateUsersTable();
                } catch (SQLException | IOException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
        };
        updateUserListButton.addActionListener(updateUserList);
    }

    private void updateUsersTable() throws SQLException, IOException, ClassNotFoundException {
        UserManagementRequest viewUsersRequest = new UserManagementRequest(UserManagementRequestType.viewUsers);
        oos.writeObject(viewUsersRequest);
        ViewUsersReply viewUsersReply = (ViewUsersReply) ois.readObject();

        if (viewUsersReply.isSuccess()){
            Object[][] usernames = listToTableData(viewUsersReply.getUsernames());
            DefaultTableModel usersModel = new DefaultTableModel(usernames, usernameColumn);
            usersTable.setModel(usersModel);
        } else{
            JOptionPane.showMessageDialog(null, viewUsersReply.getErrorMessage());
        }
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
