package ControlPanel.GUI.AdminPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import java.io.IOException;
import java.sql.SQLException;

public class AdminPane implements ControlPanelComponent {
    private int adminPaneId = 1;

    public AdminPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException, SQLException {
        new RegisterUser(controlPanelGUI);
        new ViewUsers(controlPanelGUI);
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {

    }
}
