package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import User.ClientUser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * This class is used to let the user choose the way to process the data (delete or edit),
 * and display the added or edited data
 *
 * @author Jun Chen(n10240977) & William Tran (n10306234) */
public class ViewBillboards implements ControlPanelComponent {
    Integer billboardID;
    int selectedRow = -1;

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
    public JPanel editBbColourPreview;
    public JPanel editMessageColourPreview;
    public JPanel editInfoColourPreview;
    public JButton editBbColourButton;
    public JButton editMessageColourButton;
    public JButton editInfoColourButton;
    public JButton editChooseImageButton;
    public JButton editPreviewButton;
    public JButton editUpdateButton;
    public JButton editCancelButton;
    public JButton viewXMLExportButton;
    public JButton addScheduleButton;
    public JButton viewScheduleButton;
    public JTable calenderView;
    public JButton viewAllSchedulesButton;
    public static Boolean isSpecificSchedule = false;

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
         * Implements a ActionListener to export a selected billboard to XML
         */
        viewXMLExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String billboardID = viewTable.getModel().getValueAt(selectedRow,0).toString();
                    String billboardName = viewTable.getModel().getValueAt(selectedRow,1).toString();

                    BillboardRequest getXML = new BillboardRequest(BillboardRequestType.getXML, billboardID, ClientUser.getToken());
                    //read the reply from the server
                    BillboardReply replyXML = (BillboardReply) getXML.getOIS().readObject();
                    getXML.closeConnection();
                    String result = replyXML.getMessage();

                    OutputStream out = new FileOutputStream(billboardName + ".xml");
                    try {
                        byte[] format = result.getBytes();
                        out.write(format);
                    } catch (NullPointerException exc) {
                        (new File(billboardName+ ".xml")).delete();
                        JOptionPane.showMessageDialog(controlPanel,"Cannot retrieve billboard from the database. Please contact your administrator.",
                                "Error", JOptionPane.NO_OPTION);
                    } finally {
                        out.close();
                    }

                    // Show a message
                    JOptionPane.showMessageDialog(controlPanel,"Successfully exported into: " + billboardName + ".xml"
                            ,"Information",JOptionPane.NO_OPTION);
                } catch (ArrayIndexOutOfBoundsException exc) {
                    JOptionPane.showMessageDialog(controlPanel,"Please select a billboard to export!",
                            "Error", JOptionPane.NO_OPTION);
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(controlPanel,"Cannot retrieve billboard from the database. Please contact your administrator.",
                            "Error", JOptionPane.NO_OPTION);
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
                int result = JOptionPane.showConfirmDialog(null,
                        "Are you sure to delete billboard " + billboardID + "?", "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        //sends the delete request, provided with selection id
                        Object[] id = {billboardID};
                        BillboardRequest delete = new BillboardRequest(BillboardRequestType.delete,id,ClientUser.getToken());

                        if(billboardID != null){
                            //read the reply from the server
                            BillboardReply messageObject = (BillboardReply)delete.getOIS().readObject();
                            delete.closeConnection();
                            String message = messageObject.getMessage();
                            //refresh the pane to view data
                            billboardsPane.setSelectedIndex(2);
                            billboardsPane.setSelectedIndex(0);

                            JOptionPane.showMessageDialog(controlPanel,message,"Success",JOptionPane.NO_OPTION);

                        } else{
                            //read the reply from the server
                            BillboardReply messageObject = (BillboardReply)delete.getOIS().readObject();
                            delete.closeConnection();
                            String message = messageObject.getMessage();
                            JOptionPane.showMessageDialog(controlPanel,message,"Warning",JOptionPane.NO_OPTION);
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(controlPanel,"Something went wrong. Cannot delete this billboard!",
                                "Error",JOptionPane.NO_OPTION);
                    }
                }
            }
        });

        viewScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //try request the data to view in control panel and sends to server
                try {
                    // Get the respected billboard name to view schedule and display on schedule panel for users to edit
                    String billboardID = viewTable.getModel().getValueAt(selectedRow,0).toString();

                    BillboardRequest showTableRequest = new BillboardRequest(BillboardRequestType.showSchedule, billboardID, ClientUser.getToken());
                    BillboardReply tableData = (BillboardReply) showTableRequest.getOIS().readObject();
                    Object[][]  data = tableData.getTableData();
                    showTableRequest.closeConnection();

                    calenderView.setModel(new DefaultTableModel(
                            data,
                            new String[]{"Schedule ID", "Billboard ID","Billboard Name","Date","Starting time", "Ending time"}
                    ));
                    calenderView.setDefaultEditor(Object.class, null);
                    isSpecificSchedule = true;
                    viewAllSchedulesButton.setText("View all schedules in the next 7 days");
                    viewAllSchedulesButton.setEnabled(true);
                    billboardsPane.setSelectedIndex(3);

                } catch (IOException | ClassNotFoundException | ArrayIndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(controlPanel,"Please select a billboard to view its schedule!",
                            "Warning", JOptionPane.NO_OPTION);
                }

            }
        });

        //add schedule on view tab.
        addScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                // Get the respected values and display on edit panel for users to edit
                String billboardID = viewTable.getModel().getValueAt(selectedRow,0).toString();

                JComboBox date = new JComboBox(generateInputDate());
                JComboBox startHour = new JComboBox(generateInputHour());
                JComboBox startMinute = new JComboBox(generateInputMinute());
                JComboBox endHour = new JComboBox(generateInputHour());
                JComboBox endMinute = new JComboBox(generateInputMinute());
                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("Date:"));
                myPanel.add(date);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("Start hour:"));
                myPanel.add(startHour);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("Start minute:"));
                myPanel.add(startMinute);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("End hour:"));
                myPanel.add(endHour);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("End minute:"));
                myPanel.add(endMinute);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Add schedule for billboard number " + billboardID, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    Boolean validInput = (Integer.parseInt(startHour.getSelectedItem().toString()) < Integer.parseInt(endHour.getSelectedItem().toString()))
                            || (Integer.parseInt(startHour.getSelectedItem().toString()) == Integer.parseInt(endHour.getSelectedItem().toString())
                                && Integer.parseInt(startMinute.getSelectedItem().toString()) < Integer.parseInt(endMinute.getSelectedItem().toString()));
                    if (validInput) {
                        SimpleDateFormat oldDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        //sends the request to server with data provided
                        String startTime = startHour.getSelectedItem().toString() + ":" + startMinute.getSelectedItem().toString() + ":00";
                        String endTime = endHour.getSelectedItem().toString() + ":" + endMinute.getSelectedItem().toString() + ":00";
                        String chosenScheduleDate = newDateFormat.format(oldDateFormat.parse(date.getSelectedItem().toString()));

                        Object[] submitData = {billboardID, chosenScheduleDate, startTime, endTime};
                        BillboardRequest addview = new BillboardRequest(BillboardRequestType.addSchedule, submitData, ClientUser.getToken());

                        //read the reply from the server
                        BillboardReply messageObject = (BillboardReply) addview.getOIS().readObject();
                        addview.closeConnection();
                        String message = messageObject.getMessage();
                        //refresh the pane to get the latest view
                        billboardsPane.setSelectedIndex(2);
                        billboardsPane.setSelectedIndex(0);
                        JOptionPane.showMessageDialog(controlPanel, message, "Information", JOptionPane.NO_OPTION);
                    } else {
                        JOptionPane.showMessageDialog(controlPanel, "Please enter a valid period of time!", "Information", JOptionPane.NO_OPTION);
                    }
                }

                } catch (IOException | ParseException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(controlPanel,"Something went wrong, cannot schedule this billboard!","Error",JOptionPane.NO_OPTION);
                } catch (ArrayIndexOutOfBoundsException ex){
                    JOptionPane.showMessageDialog(controlPanel,"Please select a billboard for scheduling!","Information",JOptionPane.NO_OPTION);
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
                try {
                    // Get the respected values and display on edit panel for users to edit
                    String billboardID = viewTable.getModel().getValueAt(selectedRow,0).toString();

                    // Contact to get the respective xml from the server
                    BillboardRequest getXML = new BillboardRequest(BillboardRequestType.getXML, billboardID, ClientUser.getToken());
                    //read the reply from the server
                    BillboardReply replyXML = (BillboardReply) getXML.getOIS().readObject();
                    getXML.closeConnection();
                    String result = replyXML.getMessage();

                    // Create a document builder to parse the XML file
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document doc = builder.parse(new InputSource(new StringReader(result)));
                    doc.getDocumentElement().normalize();

                    // Get background color
                    String billboardColourString = doc.getDocumentElement().getAttribute("background");
                    Color bgColour = Color.decode(billboardColourString != "" ? billboardColourString : "#ffffff");

                    // Get message data
                    String msg;
                    Color msgColour = Color.decode("#000000");;
                    try {
                        // Text
                        Element messageData = (Element) doc.getElementsByTagName("message").item(0);
                        msg = messageData.getTextContent();

                        // Colour
                        String messageColourString = messageData.getAttribute("colour");
                        msgColour = Color.decode(messageColourString != "" ? messageColourString : "#000000");
                    } catch (NullPointerException exc) {
                        // If there is nothing, return null
                        msg = null;
                    }

                    // Get picture data
                    String imageInput;
                    String imageMessage;
                    try {
                        Element pictureData = (Element) doc.getElementsByTagName("picture").item(0);
                        // URL
                        if (pictureData.hasAttribute("url")){
                            imageInput = pictureData.getAttribute("url");
                            imageMessage = imageInput;
                        } else { // Base64 encoded
                            imageInput = pictureData.getAttribute("data");
                            imageMessage = "(An image file has been chosen. Leave this field blank to not submitting it!)";
                        }
                    } catch (NullPointerException exc) {
                        // If there is nothing, return null
                        imageInput = null;
                        imageMessage = null;
                    }

                    // Get information data
                    String info;
                    Color infoColour = Color.decode("#000000");
                    try {
                        // Text
                        Element informationData = (Element) doc.getElementsByTagName("information").item(0);
                        info = informationData.getTextContent();

                        // Colour
                        String informationColourString = informationData.getAttribute("colour");
                        infoColour = Color.decode(informationColourString != "" ? informationColourString : "#000000");
                    } catch (NullPointerException exc) {
                        // If there is nothing, return null
                        info = null;
                    }

                    // Export the read information to edit billboard tab
                    editBbID.setText(billboardID);
                    editBbName.setText((String) viewTable.getModel().getValueAt(selectedRow,1));
                    editBbMsg.setText(msg);
                    editBbInfo.setText(info);
                    editBbImgLink.setText(imageMessage);
                    EditBillboards.setEditImageLink(imageInput);
                    editBbColourPreview.setBackground(bgColour);
                    editMessageColourPreview.setBackground(msgColour);
                    editInfoColourPreview.setBackground(infoColour);
                    toEditRow.setText(String.valueOf(selectedRow));

                    // Change tab and enable edit fields and buttons
                    editBbName.setEnabled(true);
                    editBbMsg.setEnabled(true);
                    editBbInfo.setEnabled(true);
                    editBbImgLink.setEnabled(true);
                    editBbColourButton.setEnabled(true);
                    editMessageColourButton.setEnabled(true);
                    editInfoColourButton.setEnabled(true);
                    editPreviewButton.setEnabled(true);
                    editUpdateButton.setEnabled(true);
                    editCancelButton.setEnabled(true);
                    editChooseImageButton.setEnabled(true);
                    billboardsPane.setSelectedIndex(2);
                } catch (ArrayIndexOutOfBoundsException exc) {
                    JOptionPane.showMessageDialog(controlPanel,"Please select a billboard to begin editing!",
                            "Error", JOptionPane.NO_OPTION);
                } catch (NullPointerException | ClassNotFoundException exc) {
                    // If the billboard is not found, show a dialog message
                    JOptionPane.showMessageDialog(controlPanel,"Cannot retrieve billboard from the database. Please contact your administrator.",
                            "Error", JOptionPane.NO_OPTION);
                } catch (ParserConfigurationException | SAXException | IOException error) {
                    JOptionPane.showMessageDialog(controlPanel,"An error occurred. Cannot read billboard. Please contact your administrator.",
                            "Error", JOptionPane.NO_OPTION);
                }
            }
        });

        /**
         * Implements a ChangeListener for tab change,
         * when tab changed to view tab, all data in viewTable get refreshed,
         * the same for schedule tab
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
                                    new String[]{"Billboard ID","Billboard Name","Creator Name","Message", "Information","Url", "Scheduled Date", "Start time", "End time"}
                            ));
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }

                if (billboardsPane.getSelectedIndex() == 3 && !isSpecificSchedule){
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
            }
        });
    }

    // Create an array of date from today to the next 7 days
    private static String[] generateInputDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String[] dateArray = new String[8];
        dateArray[0] = dateFormat.format(calendar.getTime());
        for (int i = 1; i <= 7; i++){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            String newDate = dateFormat.format(calendar.getTime());
            dateArray[i] = newDate;
        }
        return dateArray;
    }

    private static String[] generateInputHour() {
        String[] hourArray = new String[24];
        for (int i = 0; i <= 23; i++) {
            hourArray[i] = Integer.toString(i);
        }

        return hourArray;
    }

    private static String[] generateInputMinute() {
        String[] minuteArray = new String[60];
        for (int i = 0; i <= 59; i++) {
            minuteArray[i] = Integer.toString(i);
        }

        return minuteArray;
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
        this.editBbColourPreview = controlPanelGUI.editBbColourPreview;
        this.editMessageColourPreview = controlPanelGUI.editMessageColourPreview;
        this.editInfoColourPreview = controlPanelGUI.editInfoColourPreview;
        this.editBbColourButton = controlPanelGUI.editBbColourButton;
        this.editMessageColourButton = controlPanelGUI.editMessageColourButton;
        this.editInfoColourButton = controlPanelGUI.editInfoColourButton;
        this.editChooseImageButton = controlPanelGUI.editChooseImageButton;
        this.editPreviewButton = controlPanelGUI.editPreviewButton;
        this.editCancelButton = controlPanelGUI.editCancelButton;
        this.editUpdateButton = controlPanelGUI.editUpdateButton;
        this.viewXMLExportButton = controlPanelGUI.viewXMLExportButton;
        this.addScheduleButton = controlPanelGUI.addScheduleButton;
        this.viewScheduleButton = controlPanelGUI.viewScheduleButton;
        this.calenderView = controlPanelGUI.calenderView;
        this.viewAllSchedulesButton = controlPanelGUI.viewAllSchedulesButton;
    }
}
