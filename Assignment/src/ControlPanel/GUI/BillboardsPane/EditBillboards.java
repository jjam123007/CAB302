package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import Networking.Request;
import User.ClientUser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


/**
 *This class used to edit exist billBoards
 * @author Jun Chen(n10240977)&Haoze He(n10100351)
 */
public class EditBillboards extends Request implements ControlPanelComponent {

    private JPanel controlPanel;
    public JTabbedPane billboardsPane;
    public JTextArea editBbName;
    public JTextArea editBbMsg;
    public JTextArea editBbInfo;
    public JTextArea editBbImgLink;
    public JButton editUpdateButton;
    public JTextArea editBbID;
    public JTable viewTable;
    public JTextArea toEditRow;
    public JPanel EditJPanel;

    public int rowToEdit;

    /**
     *
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public EditBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);
        toEditRow.setText("Edit through create billboard menu");
        toEditRow.setEnabled(false);

        editUpdateButton.addActionListener(new ActionListener() {
            /**
             * Implements a ActionListener for updateButton to update data is changed in view interface
             * @param e
             */
            @Override
            /**
             * @see javax.awt.event.addActionListener#actionPerformed(javax.awt.event.ActionListener)
             */
            public void actionPerformed(ActionEvent e) {
                int viewId = Integer.valueOf(editBbID.getText());
                String billboardName = editBbName.getText();
                String billboardMessage = editBbMsg.getText();
                String billboardInformation = editBbInfo.getText();
                String billboardUrl = editBbImgLink.getText();
                rowToEdit = Integer.valueOf(toEditRow.getText());
                try {
                    Object[] newTable = {viewId,billboardName,billboardMessage,billboardInformation,billboardUrl};
                    BillboardRequest edit = new BillboardRequest(BillboardRequestType.edit, newTable, ClientUser.getToken());

                    System.out.println("ROW TO EDIT"+rowToEdit);
                    viewTable.getModel().setValueAt(viewId,rowToEdit,0);
                    viewTable.getModel().setValueAt(billboardName,rowToEdit,1);
                    viewTable.getModel().setValueAt(billboardMessage,rowToEdit,2);
                    viewTable.getModel().setValueAt(billboardInformation,rowToEdit,3);
                    viewTable.getModel().setValueAt(billboardUrl,rowToEdit,4);
                    rowToEdit = -1;
                    //read the reply from the server
                    BillboardReply messageObject = (BillboardReply) edit.getOIS().readObject();
                    edit.closeConnection();
                    String message = messageObject.getMessage();
                    System.out.println("Message: "+message);
                    editBbName.setText("");
                    editBbMsg.setText("");
                    editBbInfo.setText("");
                    editBbImgLink.setText("");
                    editBbID.setText("");

                    JOptionPane.showMessageDialog(controlPanel,message,"message",JOptionPane.NO_OPTION);


                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                billboardsPane.setSelectedIndex(0);
            }
        });
    }

    /**
     * setter function to set value for GUI elements and variables
     * @param controlPanelGUI
     */
    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.controlPanel = controlPanelGUI.controlPanel;
        this.billboardsPane = controlPanelGUI.billboardsPane;
        this.editBbName = controlPanelGUI.editBbName;
        this.editBbMsg = controlPanelGUI.editBbMsg;
        this.editBbInfo = controlPanelGUI.editBbInfo;
        this.editBbImgLink = controlPanelGUI.editBbImgLink;
        this.editUpdateButton = controlPanelGUI.editUpdateButton;
        this.editBbID = controlPanelGUI.editBbID;
        this.viewTable = controlPanelGUI.viewTable;
        this.toEditRow = controlPanelGUI.toEditRow;
        this.EditJPanel = controlPanelGUI.EditJPanel;

    }
}
