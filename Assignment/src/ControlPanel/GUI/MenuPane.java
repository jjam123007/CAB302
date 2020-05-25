package ControlPanel.GUI;

import ControlPanel.GUI.AdminPane.AdminPane;
import ControlPanel.GUI.BillboardsPane.BillboardsPane;
import User.ClientUser;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class MenuPane implements ControlPanelComponent{
    private int adminPaneId = 1;
    JTabbedPane menuPane;


    public MenuPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException, SQLException {
        setControlPanelComponents(controlPanelGUI);
        new BillboardsPane(controlPanelGUI);
        new AccountPane(controlPanelGUI);
        if (ClientUser.getPermissions().isAdmin()){
            new AdminPane(controlPanelGUI);
        } else {
            menuPane.remove(adminPaneId);
        }
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.menuPane = controlPanelGUI.menuPane;

    }
}
