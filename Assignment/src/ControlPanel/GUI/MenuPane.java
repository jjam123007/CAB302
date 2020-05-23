package ControlPanel.GUI;

import ControlPanel.GUI.AdminPane.AdminPane;
import ControlPanel.GUI.AdminPane.RegisterUser;
import UserManagement.ClientUser;

import java.io.IOException;

public class MenuPane extends ControlPanelGUI {
    private int adminPaneId = 1;

    public MenuPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        super(controlPanelGUI);
        if (ClientUser.getPermissions().isAdmin()){
            new AdminPane(controlPanelGUI);
        } else {
            menuPane.remove(adminPaneId);
        }
    }
}
