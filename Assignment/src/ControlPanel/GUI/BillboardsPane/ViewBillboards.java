package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import User.ClientUser;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


/**
 * This class is used to let the user choose the way to process the data (delete or edit),
 * and display the added or edited data
 *
 * @author Jun Chen(n10240977) */
public class ViewBillboards implements ControlPanelComponent {
    Integer billboardID;
    int selectedRow;

    public JTabbedPane billboardsPane;
    public JTextArea editBbMsg;
    public JTextArea editBbInfo;
    public JTextArea editBbImgLink;
    public JTextArea editBbName;
    public JLabel editBbID;
    public JTable viewTable;
    public JButton viewEditButton;
    public JButton viewDeleteButton;
    public JLabel toEditRow;
    public JPanel controlPanel;


    /**
     *
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ViewBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);

        //try request the data to view in control panel and sends to server
        try {
            BillboardRequest showTableRequest = new BillboardRequest(BillboardRequestType.showTable,null, ClientUser.getToken());
            BillboardReply tableData = (BillboardReply) showTableRequest.getOIS().readObject();
            Object[][]  data = tableData.getTableData();

            viewTable.setModel(new DefaultTableModel(
                    data,
                    new String[]{"view ID","Billboard Name","Creator Name","Message", "Information","Url", "Scheduled Date", "Start time", "End time"}
            ));
            viewTable.setDefaultEditor(Object.class, null);

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }


        /**
         * Implements a ListSelectionListener for making the UI respond when a
         * different row is selected from the table.
         */
        viewTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            /*** @see javax.awt.event.addListSelectionListener#valueChanged(javax.awt.event.ListSelectionEvent) */
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    int i = viewTable.getSelectedRow();
                    //get the selected row when value changed
                    if (i >= 0) {
                        billboardID = Integer.parseInt(viewTable.getValueAt(viewTable.getSelectedRow(),0).toString());
                        selectedRow = viewTable.getSelectedRow();
                    }
                }
            }
        });
        /**
         * Implements a ActionListener for viewDeleteButton to delete the selected row.
         */
        viewDeleteButton.addActionListener(new ActionListener() {
            /**@param e */
            @Override
            /**@see javax.awt.event.addActionListner#actionPerformed(javax.awt.event.ActionEvent)*/
            public void actionPerformed(ActionEvent e) {
                try {
                    //sends the delete request, provided with selection id
                    Object[] id = {billboardID};
                    BillboardRequest delete = new BillboardRequest(BillboardRequestType.delete,id,ClientUser.getToken());

                    if(billboardID != null){
                        //remove the row selected on control panel
                        DefaultTableModel dm = (DefaultTableModel) viewTable.getModel();
                        dm.removeRow(selectedRow);
                        //read the reply from the server
                        BillboardReply messageObject = (BillboardReply)delete.getOIS().readObject();
                        delete.closeConnection();
                        String message = messageObject.getMessage();
                        JOptionPane.showMessageDialog(controlPanel,message,"Success",JOptionPane.NO_OPTION);
                    }else{
                        //read the reply from the server
                        BillboardReply messageObject = (BillboardReply)delete.getOIS().readObject();
                        delete.closeConnection();
                        String message = messageObject.getMessage();
                        JOptionPane.showMessageDialog(controlPanel,message,"Warning",JOptionPane.NO_OPTION);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

        });
        /**
         * Implements a ActionListener for BillboardButton to get data from the selected row
         * then put these into edit interface.
         */
        viewEditButton.addActionListener(new ActionListener() {
            /**@param e*/
            @Override
            /**@see javax.awt.event.addActionListener#actionPerformed(javax.awt.event.ActionListener)*/
            public void actionPerformed(ActionEvent e) {
                //get the respected values and display on edit panel for users to edit
                String billboardId = viewTable.getModel().getValueAt(selectedRow,0).toString();
                String billboardName = (String) viewTable.getModel().getValueAt(selectedRow,1);
                String billboardMessage = (String) viewTable.getModel().getValueAt(selectedRow,3);
                String billboardInformation = (String) viewTable.getModel().getValueAt(selectedRow,4);
                String billboardUrl = (String) viewTable.getModel().getValueAt(selectedRow,5);


                editBbID.setText(billboardId);
                editBbName.setText(billboardName);
                editBbMsg.setText(billboardMessage);
                editBbInfo.setText(billboardInformation);
                editBbImgLink.setText(billboardUrl);

                toEditRow.setText(String.valueOf(selectedRow));
                billboardsPane.setSelectedIndex(2);
            }
        });
        /**
         * Implements a ChangeListener for tab change,
         * when tab changed to view tab, all data in viewTable get refreshed
         */
        billboardsPane.addChangeListener(new ChangeListener() {
            /**@param e */
            @Override
            /**
             *
             * @see javax.awt.event.addChangeListener#stateChanged(javax.awt.event.ChangeListener) */
            public void stateChanged(ChangeEvent e) {
                if(billboardsPane.getSelectedIndex() == 0){
                    try {
                        //sends the request to server when values changed. depicted on VIEW for users to see what has changed
                        BillboardRequest showTableRequest = new BillboardRequest(BillboardRequestType.showTable,null, ClientUser.getToken());
                        BillboardReply tableData = (BillboardReply) showTableRequest.getOIS().readObject();
                        Object[][]  data = tableData.getTableData();
                        if (data!=null)
                        {
                            viewTable.setModel(new DefaultTableModel(
                                    data,
                                    new String[]{"view ID","Billboard Name","Creator Name","Message", "Information","Url", "Scheduled Date", "Start time", "End time"}
                            ));
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * setter function to set value for GUI elements and variables
     * @param controlPanelGUI */
    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {

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
