package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

public class ScheduleBillboards extends ControlPanelGUI {

    public ScheduleBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        super(controlPanelGUI);

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
                    BillboardRequest addview = new BillboardRequest(BillboardRequestType.addView, submitData, "");
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

}
