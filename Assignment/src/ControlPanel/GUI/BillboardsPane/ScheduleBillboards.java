package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;

import User.ClientUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.net.UnknownHostException;
/**
 * This class used to edit or add scheduleDate,
 * scheduledStartTime and scheduledEndTime for exist billBoards or new billBoards
 *
 * @author Jun Chen(n10240977) */
public class ScheduleBillboards implements ControlPanelComponent {

    private JPanel controlPanel;
    public JTextArea scheduleBbID;
    public JTextArea scheduleEndTime;
    public JTextArea scheduleStartTime;
    public JTextArea scheduleBbDate;
    public JButton scheduleSubmitButton;
    public JTabbedPane billboardsPane;
    public JTable calenderView;

    /**
     *
     *
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ScheduleBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);

        scheduleSubmitButton.addActionListener(new ActionListener() {
            /**
             *Implements a ActionListener for submitButton to upload scheduleDate, startTime, endTime to database and show in view interface
             * @param e
             */
            @Override
            /**
             * @see javax.awt.event.addActionListener#actionPerformed(javax.awt.event.ActionListener)
             */
            public void actionPerformed(ActionEvent e) {
                try {

                    //get the text field inputs
                    String billboardId = scheduleBbID.getText();
                    String scheduledDate = scheduleBbDate.getText();
                    String startTime = scheduleStartTime.getText();
                    String endTime = scheduleEndTime.getText();
                    String requestType = "addView";
                    //sends the request to server with data provided
                    Object[] submitData = {billboardId, scheduledDate,startTime,endTime,requestType};
                    BillboardRequest addview = new BillboardRequest(BillboardRequestType.addView, submitData, ClientUser.getToken());

                    //read the reply from the server
                    BillboardReply messageObject = (BillboardReply) addview.getOIS().readObject();
                    addview.closeConnection();
                    String message = messageObject.getMessage();

                    JOptionPane.showMessageDialog(controlPanel,message,"Information",JOptionPane.NO_OPTION);

                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                //empty the text field after successful request
                scheduleBbID.setText("");
                scheduleBbDate.setText("");
                scheduleStartTime.setText("");
                scheduleEndTime.setText("");
                billboardsPane.setSelectedIndex(0);

            }
        });
    }

    /**
     *setter function to set value for GUI elements and variables
     * @param controlPanelGUI
     */
    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {

        this.controlPanel = controlPanelGUI.controlPanel;
        this.scheduleBbID = controlPanelGUI.scheduleBbID;
        this.scheduleEndTime = controlPanelGUI.scheduleEndTime;
        this.scheduleStartTime = controlPanelGUI.scheduleStartTime;
        this.scheduleBbDate = controlPanelGUI.scheduleBbDate;
        this.scheduleSubmitButton = controlPanelGUI.scheduleSubmitButton;
        this.billboardsPane=controlPanelGUI.billboardsPane;
        this.calenderView = controlPanelGUI.calenderView;
    }
}
