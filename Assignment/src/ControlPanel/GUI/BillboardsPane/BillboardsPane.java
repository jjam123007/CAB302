package ControlPanel.GUI.BillboardsPane;

import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;

import javax.swing.*;
import java.io.IOException;


/**
 * This class create a pane to store view, create, edit, schedule for billBoard.
 * @author Jun Chen(n10240977)
 */
public class BillboardsPane implements ControlPanelComponent {

    public JTabbedPane billboardsPane;
    public JTable viewTable;

    /**
     *
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public BillboardsPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        new CreateBillboards(controlPanelGUI);
        new EditBillboards(controlPanelGUI);
        new ScheduleBillboards(controlPanelGUI);
        new ViewBillboards(controlPanelGUI);
    }

    /**
     *
     * @param controlPanelGUI
     */
    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {

    }


}
