package ControlPanel.GUI.AdminPane;

import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import ControlPanel.SerializeArray;
import UserManagement.ViewUsersRequest;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class ViewUsers implements ControlPanelComponent {
    private JTable usersTable;
    String usernameColumn[] = {"Username"};

    public ViewUsers(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);
    }

    public void getUsers(){
        //new ViewUsersRequest() = new ViewUsersRequest();
        Object[][] data = {{"we"}};
        DefaultTableModel usersModel = new DefaultTableModel(data, usernameColumn);
        usersTable.setModel(usersModel);
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.usersTable = controlPanelGUI.usersTable;
    }
}
