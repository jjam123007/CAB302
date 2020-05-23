package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelGUI;
import UserManagement.ClientUser;

import java.io.IOException;

public class AdminPane extends ControlPanelGUI {
    private int adminPaneId = 1;

    public AdminPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        super(controlPanelGUI);
        new RegisterUser(controlPanelGUI);
    }
}
