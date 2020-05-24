package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import User.ClientUser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

public class ScheduleBillboards implements ControlPanelComponent {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JPanel controlPanel;
    public JTextArea scheduleBbID;
    public JTextArea scheduleEndTime;
    public JTextArea scheduleStartTime;
    public JTextArea scheduleBbDate;
    public JButton scheduleSubmitButton;

    public ScheduleBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);

        scheduleSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String billboardId = scheduleBbID.getText();
                    String scheduledDate = scheduleBbDate.getText();
                    String startTime = scheduleStartTime.getText();
                    String endTime = scheduleEndTime.getText();
                    String requestType = "addView";
                    Object[] submitData = {billboardId, scheduledDate,startTime,endTime,requestType};
                    BillboardRequest addview = new BillboardRequest(BillboardRequestType.addView, submitData, ClientUser.getToken());
                    oos.writeObject(addview);
                    oos.flush();
                    JOptionPane.showMessageDialog(controlPanel,"Success","message",JOptionPane.NO_OPTION);
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.oos = controlPanelGUI.oos;
        this.ois = controlPanelGUI.ois;
        this.controlPanel = controlPanelGUI.controlPanel;
        this.scheduleBbID = controlPanelGUI.scheduleBbID;
        this.scheduleEndTime = controlPanelGUI.scheduleEndTime;
        this.scheduleStartTime = controlPanelGUI.scheduleStartTime;
        this.scheduleBbDate = controlPanelGUI.scheduleBbDate;
        this.scheduleSubmitButton = controlPanelGUI.scheduleSubmitButton;
    }
}
