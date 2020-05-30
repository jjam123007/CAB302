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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
/**@author Jun Chen(n10240977)&Haoze He(n10100351) */
public class ScheduleBillboards extends Request implements ControlPanelComponent {

    private JPanel controlPanel;
    public JTextArea scheduleBbID;
    public JTextArea scheduleEndTime;
    public JTextArea scheduleStartTime;
    public JTextArea scheduleBbDate;
    public JButton scheduleSubmitButton;
    public JTabbedPane billboardsPane;

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
                    String billboardId = scheduleBbID.getText();
                    String scheduledDate = scheduleBbDate.getText();
                    String startTime = scheduleStartTime.getText();
                    String endTime = scheduleEndTime.getText();
                    String requestType = "addView";
                    Object[] submitData = {billboardId, scheduledDate,startTime,endTime,requestType};
                    BillboardRequest addview = new BillboardRequest(BillboardRequestType.addView, submitData, ClientUser.getToken());

                    //read the reply from the server
                    BillboardReply messageObject = (BillboardReply) addview.getOIS().readObject();
                    addview.closeConnection();
                    String message = messageObject.getMessage();
                    System.out.println("Message: "+message);
                    JOptionPane.showMessageDialog(controlPanel,message,"Information",JOptionPane.NO_OPTION);

                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
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
    }
}
