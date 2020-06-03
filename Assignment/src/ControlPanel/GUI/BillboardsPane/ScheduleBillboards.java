package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;

import User.ClientUser;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static ControlPanel.GUI.BillboardsPane.ViewBillboards.isSpecificSchedule;

/**
 * This class used to edit or add scheduleDate,
 * scheduledStartTime and scheduledEndTime for exist billBoards or new billBoards
 *
 * @author Jun Chen(n10240977) & William Tran (n10306234)
 */
public class ScheduleBillboards implements ControlPanelComponent {
    private JPanel controlPanel;
    public JTabbedPane billboardsPane;
    public JTable calenderView;
    public JButton viewAllSchedulesButton;
    public JButton deleteScheduleButton;

    public int selectedRow = -1;

    /**
     *
     *
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ScheduleBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);

        /**
         * Implements a ListSelectionListener for making the UI respond when a
         * different row is selected from the table.
         */
        calenderView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            /*** @see javax.awt.event.addListSelectionListener#valueChanged(javax.awt.event.ListSelectionEvent) */
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    int i = calenderView.getSelectedRow();
                    //get the selected row when value changed
                    if (i >= 0) {
                        selectedRow = calenderView.getSelectedRow();
                    }
                }
            }
        });

        /**
         * Add action listener to the button to view all schedules from the current day to the next 7 days
         */
        viewAllSchedulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSpecificSchedule = false;
                try {
                    BillboardRequest request = new BillboardRequest(BillboardRequestType.showAllSchedules, "dummy data", ClientUser.getToken());
                    BillboardReply tableData = (BillboardReply) request.getOIS().readObject();
                    Object[][]  data = tableData.getTableData();
                    request.closeConnection();

                    calenderView.setModel(new DefaultTableModel(
                            data,
                            new String[]{"Schedule ID", "Billboard ID","Billboard Name","Date","Starting time", "Ending time"}
                    ));
                    calenderView.setDefaultEditor(Object.class, null);
                    viewAllSchedulesButton.setText("Currently viewing all schedules in the next 7 days");
                    viewAllSchedulesButton.setEnabled(false);
                    billboardsPane.setSelectedIndex(3);
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(controlPanel,"Cannot read data from the server.","Error",JOptionPane.NO_OPTION);
                }
            }
        });

        /**
         * Add action listener to the button to delete a selected schedule
         */
        deleteScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Are you sure to delete schedule for billboard " + calenderView.getModel().getValueAt(selectedRow,1).toString()
                                + ", from " + calenderView.getModel().getValueAt(selectedRow,4).toString() + " to "
                                + calenderView.getModel().getValueAt(selectedRow,5).toString() + " on "
                                + calenderView.getModel().getValueAt(selectedRow,3).toString() + "?",
                            "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        Object[] id = {calenderView.getModel().getValueAt(selectedRow, 0).toString()};

                        BillboardRequest delete = new BillboardRequest(BillboardRequestType.deleteSchedule, id, ClientUser.getToken());
                        BillboardReply messageObject = (BillboardReply) delete.getOIS().readObject();
                        delete.closeConnection();
                        String message = messageObject.getMessage();

                        //refresh the pane to view data
                        billboardsPane.setSelectedIndex(1);
                        billboardsPane.setSelectedIndex(3);

                        JOptionPane.showMessageDialog(controlPanel, message, "Success", JOptionPane.NO_OPTION);

                    } catch (ArrayIndexOutOfBoundsException exc) {
                        JOptionPane.showMessageDialog(controlPanel, "Please select a schedule to delete!",
                                "Error", JOptionPane.NO_OPTION);
                    } catch (IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(controlPanel, "Something went wrong. Cannot delete this schedule!",
                                "Error", JOptionPane.NO_OPTION);
                    }
                }
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
        this.billboardsPane=controlPanelGUI.billboardsPane;
        this.calenderView = controlPanelGUI.calenderView;
        this.viewAllSchedulesButton = controlPanelGUI.viewAllSchedulesButton;
        this.deleteScheduleButton = controlPanelGUI.deleteScheduleButton;
    }
}
