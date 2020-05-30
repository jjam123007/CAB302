package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import ControlPanel.SerializeArray;
import User.ClientUser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;


/**
 * @author Jun Chen(n10240977)&Haoze He(n10100351)
 */
public class BillboardsPane implements ControlPanelComponent {
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
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
