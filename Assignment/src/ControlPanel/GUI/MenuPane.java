package ControlPanel.GUI;

import ControlPanel.GUI.AdminPane.AdminPane;
import ControlPanel.GUI.AdminPane.RegisterUser;
import ControlPanel.GUI.BillboardsPane.BillboardsPane;
import UserManagement.ClientUser;

import javax.swing.*;
import java.io.IOException;

public class MenuPane implements ControlPanelComponent{
    private int adminPaneId = 1;
    JTabbedPane menuPane;

    public MenuPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        new BillboardsPane(controlPanelGUI);

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
