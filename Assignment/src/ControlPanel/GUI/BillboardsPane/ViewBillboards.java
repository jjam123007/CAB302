package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import ControlPanel.SerializeArray;
import User.ClientUser;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ViewBillboards implements ControlPanelComponent {
    Integer billboardID;
    int selectedRow;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public JTabbedPane billboardsPane;
    public JTextArea editBbMsg;
    public JTextArea editBbInfo;
    public JTextArea editBbImgLink;
    public JTextArea editBbName;
    public JTextArea editBbID;
    public JTable viewTable;
    public JButton viewEditButton;
    public JButton viewDeleteButton;
    public JTextArea toEditRow;
    public int rowToEdit;


    public ViewBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);

        BillboardRequest request = new BillboardRequest(BillboardRequestType.showTable,null, ClientUser.getToken());
        oos.writeObject(request);
        oos.flush();
        System.out.println("hello345");
        SerializeArray tableData = (SerializeArray) ois.readObject();
        Object[][]  data = tableData.getData();
        System.out.println("hello345");
        viewTable.setModel(new DefaultTableModel(
                data,
                new String[]{"BillboardID","Billboard Name","Creator Name","Information","Message", "Url", "Scheduled Date", "Start time", "End time"}
        ));

        viewTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    int i = viewTable.getSelectedRow();
                    if (i >= 0) {
                        billboardID = Integer.parseInt(viewTable.getValueAt(viewTable.getSelectedRow(),0).toString());
                        selectedRow = viewTable.getSelectedRow();
                        System.out.println("listener selected row "+selectedRow);

                    }
                }
            }
        });
        viewTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                billboardID = Integer.valueOf(viewTable.getModel().getValueAt(row,0).toString());
            }
        });

        viewDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("hello345");
                    Object[] id = {billboardID};
                    BillboardRequest delete = new BillboardRequest(BillboardRequestType.delete,id,ClientUser.getToken());
                    oos.writeObject(delete);
                    oos.flush();

                    if(billboardID != null){
                        System.out.println("Selected id "+billboardID);
                        DefaultTableModel dm = (DefaultTableModel) viewTable.getModel();
                        System.out.println("row "+selectedRow);
                        dm.removeRow(selectedRow);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });

        viewEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String billboardId = viewTable.getModel().getValueAt(selectedRow,0).toString();
                String billboardName = (String) viewTable.getModel().getValueAt(selectedRow,1);
                String billboardMessage = (String) viewTable.getModel().getValueAt(selectedRow,3);
                String billboardInformation = (String) viewTable.getModel().getValueAt(selectedRow,4);
                String billboardUrl = (String) viewTable.getModel().getValueAt(selectedRow,5);

                editBbID.setText(billboardId);
                editBbID.setEditable(false);
                editBbName.setText(billboardName);
                editBbMsg.setText(billboardMessage);
                editBbInfo.setText(billboardInformation);
                editBbImgLink.setText(billboardUrl);
                toEditRow.setText(String.valueOf(selectedRow));
                toEditRow.setEditable(false);
                billboardsPane.setSelectedIndex(2);
            }
        });

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
                                new String[]{"view ID","Billboard Name","Creator Name","Message", "Information","Url", "Scheduled Date", "Start time", "End time"}
                        ));

                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
    }


    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.oos = controlPanelGUI.oos;
        this.ois = controlPanelGUI.ois;
        this.billboardsPane = controlPanelGUI.billboardsPane;
        this.editBbMsg = controlPanelGUI.editBbMsg;
        this.editBbInfo = controlPanelGUI.editBbInfo;
        this.editBbImgLink = controlPanelGUI.editBbImgLink;
        this.editBbID = controlPanelGUI.editBbID;
        this.editBbName = controlPanelGUI.editBbName;
        this.viewTable = controlPanelGUI.viewTable;
        this.viewEditButton = controlPanelGUI.viewEditButton;
        this.viewDeleteButton = controlPanelGUI.viewDeleteButton;
        this.toEditRow = controlPanelGUI.toEditRow;
    }
}
