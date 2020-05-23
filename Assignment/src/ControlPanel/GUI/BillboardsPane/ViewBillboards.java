package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelGUI;
import ControlPanel.SerializeArray;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public class ViewBillboards extends ControlPanelGUI{
    private Integer billboardID;
    private int selectedRow;

    public ViewBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        super(controlPanelGUI);
        BillboardRequest request = new BillboardRequest(BillboardRequestType.showTable,null,"");
        oos.writeObject(request);
        oos.flush();
        System.out.println("hello345");
        SerializeArray tableData = (SerializeArray) ois.readObject();
        Object[][]  data = tableData.getData();
        System.out.println("hello345");
        viewTable.setModel(new DefaultTableModel(
                data,
                new String[]{"BillboardID","Billboard Name","Information","Message", "Url", "Scheduled Date", "Start time", "End time"}
        ));

        viewTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    int i = viewTable.getSelectedRow();
                    if (i >= 0) {
                        billboardID = Integer.parseInt(viewTable.getValueAt(viewTable.getSelectedRow(),0).toString());
                        selectedRow = viewTable.getSelectedRow();
                    }
                }
            }
        });
        viewTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

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
                    BillboardRequest delete = new BillboardRequest(BillboardRequestType.delete,id,"");
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
                String billboardMessage = (String) viewTable.getModel().getValueAt(selectedRow,2);
                String billboardInformation = (String) viewTable.getModel().getValueAt(selectedRow,3);
                String billboardUrl = (String) viewTable.getModel().getValueAt(selectedRow,4);

                editBbID.setText(billboardId);
                editBbID.setEditable(false);
                editBbName.setText(billboardName);
                editBbMsg.setText(billboardMessage);
                editBbInfo.setText(billboardInformation);
                editBbImgLink.setText(billboardUrl);
                rowToEdit = selectedRow;
                billboardsPane.setSelectedIndex(2);
            }
        });
    }
}
