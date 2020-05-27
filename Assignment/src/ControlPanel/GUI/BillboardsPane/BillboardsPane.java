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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class BillboardsPane implements ControlPanelComponent {
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    public JTabbedPane billboardsPane;
    public JTable viewTable;

    public BillboardsPane(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        new CreateBillboards(controlPanelGUI);
        new EditBillboards(controlPanelGUI);
        new ScheduleBillboards(controlPanelGUI);
        new ViewBillboards(controlPanelGUI);
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        billboardsPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("tab: " + billboardsPane.getSelectedIndex());
                if(billboardsPane.getSelectedIndex() == 0){

                    try {

                        BillboardRequest request = new BillboardRequest(BillboardRequestType.showTable,null, ClientUser.getToken());
                        oos.writeObject(request);
                        oos.flush();
                        System.out.println("table tabbed");
                        SerializeArray tableData = (SerializeArray) ois.readObject();
                        Object[][]  data = tableData.getData();

                        viewTable.setModel(new DefaultTableModel(
                                data,
                                new String[]{"View ID","Billboard Name","Creator Name","Message","Information", "Url", "Scheduled Date", "Start time", "End time"}
                        ));

                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });

    }


}
