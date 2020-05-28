package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import java.io.IOException;
import java.sql.SQLException;
/**
 * @author Nikolai Taufao | N10481087
 */
public class AdminPane implements ControlPanelComponent {
    private int adminPaneId = 1;

    /**
     * Create the admin tab which allows admins to create, delete and change the permissions and passwords of system users.
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public AdminPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException, SQLException {
        new RegisterUser(controlPanelGUI);
        new EditUsers(controlPanelGUI);
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {

    }
}
