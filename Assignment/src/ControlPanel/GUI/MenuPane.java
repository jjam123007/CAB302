package ControlPanel.GUI;

import ControlPanel.GUI.AccountPane.AccountPane;
import ControlPanel.GUI.AdminPane.AdminPane;
import ControlPanel.GUI.BillboardsPane.BillboardsPane;
import User.ClientUser;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Nikolai Taufao | N10481087
 */
public class MenuPane implements ControlPanelComponent{
    private int adminPaneId = 1;
    JTabbedPane menuPane;

    /**
     * Create the menu tab which contains the billboard, admin and account sections.
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public MenuPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException, SQLException {
        setControlPanelComponents(controlPanelGUI);

        new BillboardsPane(controlPanelGUI);
        new AccountPane(controlPanelGUI);
        //Only show the admin pane if the client user is an admin.
        System.out.println(ClientUser.getUsername());
        System.out.println(ClientUser.getPermissions());
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
