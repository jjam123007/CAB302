package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelGUI;
import UserManagement.ClientUser;

import java.io.IOException;

public class AdminPane extends ControlPanelGUI {
    public AdminPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        super(controlPanelGUI);
        if (ClientUser.getPermissions().isAdmin()){
            new RegisterUser(controlPanelGUI);
            System.out.println("admin");
        } else {
            adminPane.setVisible(false);
            System.out.println("notAdmin");
        }

    }
}
