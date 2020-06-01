package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import BillboardViewer.BillboardViewer;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import User.ClientUser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.UnknownHostException;

/**
 * This class used to create and preview a new billBoard
 * @author Haoze He(n10100351) & William Tran (n10306234)
 */
public class CreateBillboards implements ControlPanelComponent {
    // Components
    private JPanel controlPanel;
    private JTextArea createBbName;
    private JTextArea createBbMsg;
    private JTextArea createBbInfo;
    private JTextArea createBbImgLink;
    private JButton createBbSubmitButton;
    private JButton createBbPreviewButton;
    private JButton exportToXMLButton;
    private JButton importXMLButton;
    public JTabbedPane billboardsPane;

    private JButton billboardColourButton;
    private JButton messageColourButton;
    private JButton informationColourButton;
    private JButton chooseAFileButton;
    private JPanel infoColourPreview;
    private JPanel msgColourPreview;
    private JPanel bgColourPreview;

    // Global variables
    private String msg;
    private String info;
    private String imageLink;
    private Color msgColour;
    private Color infoColour;
    private Color bgColour;

    /**
     *
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public CreateBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);

        /**
         * Add action listeners to choose the colour for the billboard, message and information
         */
        billboardColourButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create a colour chooser dialog
                    Color newColor = JColorChooser.showDialog(
                            ControlPanelGUI.frame,
                            "Choose Background Color",
                            bgColourPreview.getBackground());

                    // Set the preview colour
                    if (newColor != null) {
                        bgColourPreview.setBackground(newColor);
                    }
                }
            }
        );

        messageColourButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create a colour chooser dialog
                    Color newColor = JColorChooser.showDialog(
                            ControlPanelGUI.frame,
                            "Choose Background Color",
                            msgColourPreview.getBackground());

                    // Set the preview colour
                    if (newColor != null) {
                        msgColourPreview.setBackground(newColor);
                    }
                }
            }
        );

        informationColourButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create a colour chooser dialog
                    Color newColor = JColorChooser.showDialog(
                            ControlPanelGUI.frame,
                            "Choose Background Color",
                            infoColourPreview.getBackground());

                    // Set the preview colour
                    if (newColor != null) {
                        infoColourPreview.setBackground(newColor);
                    }
                }
            }
        );


        createBbSubmitButton.addActionListener(new ActionListener() {
            /**
             * Implements a ActionListener for submitButton to add new data in database while showing in the view interface,
             * and clear all input after submitted
             * @param e
             */
            @Override
            /**
             * @see javax.awt.event.addActionListener#actionPerformed(javax.awt.event.ActionListener)
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    String billboardName = createBbName.getText();
                    String msg = createBbMsg.getText();
                    String info = createBbInfo.getText();
                    String url = createBbImgLink.getText();
                    String billboardColour = convertColorToHexadecimal(bgColourPreview.getBackground());
                    String messageColour = convertColorToHexadecimal(msgColourPreview.getBackground());
                    String informationColour = convertColorToHexadecimal(infoColourPreview.getBackground());

                    String xml = createXMLString(billboardColour, msg, messageColour, info, informationColour, url);

                    Object[] newTable = {billboardName,msg,info,url,xml};
                    BillboardRequest addBillboard = new BillboardRequest(BillboardRequestType.addBillboard, newTable, ClientUser.getToken());
                    //read the reply from the server
                    BillboardReply messageObject = (BillboardReply) addBillboard.getOIS().readObject();
                    addBillboard.closeConnection();
                    String message = messageObject.getMessage();
                    JOptionPane.showMessageDialog(controlPanel,message,"Information",JOptionPane.NO_OPTION);

                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                createBbName.setText("");
                createBbMsg.setText("");
                createBbInfo.setText("");
                createBbImgLink.setText("");


            }
        });


        importXMLButton.addActionListener(new ActionListener() {
            /**
             * Implements a ActionListener for importXmlButton to get billBoard data,
             * then put into create interface from xml form file.
             * @param e
             */
            @Override
            /**
             * @see javax.awt.event.addActionListener#actionPerformed(javax.awt.event.ActionListener)
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    //Create a file chooser
                    final JFileChooser fc = new JFileChooser();
                    fc.showOpenDialog(null);
                    File file = fc.getSelectedFile();
                    String filename = file.getAbsolutePath();
                    //check if its xml file
                    if (getFileExtension(filename).contentEquals("xml")) {
                        // Create a document builder to parse the XML file
                        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        Document doc = builder.parse(file);
                        doc.getDocumentElement().normalize();

                        // Get background color
                        String billboardColourString = doc.getDocumentElement().getAttribute("background");
                        bgColour = Color.decode(billboardColourString != "" ? billboardColourString : "#ffffff");

                        // Get message data
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
                        try {
                            Element pictureData = (Element) doc.getElementsByTagName("picture").item(0);
                            // URL
                            if (pictureData.hasAttribute("url")){
                                imageLink = pictureData.getAttribute("url");
                            } else { // Base64 encoded
                                imageLink = pictureData.getAttribute("data");
                            }
                        } catch (NullPointerException exc) {
                            // If there is nothing, return null
                            imageLink = null;
                        }

                        // Get information data
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

                        // Set the retrieved information to the fields
                        createBbName.setText(file.getName().substring(0, file.getName().length() - 4));
                        createBbMsg.setText(msg);
                        createBbInfo.setText(info);
                        createBbImgLink.setText(imageLink);
                        bgColourPreview.setBackground(bgColour);
                        msgColourPreview.setBackground(msgColour);
                        infoColourPreview.setBackground(infoColour);

                        // Show a notification
                        JOptionPane.showMessageDialog(controlPanel,"Successful imported","Information",JOptionPane.NO_OPTION);
                    } else {
                        //if its not xml file. error
                        JOptionPane.showMessageDialog(controlPanel,"Only XML formatted files are allowed","Error",JOptionPane.NO_OPTION);
                    }
                }catch (NullPointerException | ParserConfigurationException | SAXException | IOException error){
                    error.printStackTrace();
                }

            }
        });


        exportToXMLButton.addActionListener(new ActionListener() {
            /**
             * Implements a ActionListener for exportXmlButton to get all billBoard data filled by the user,
             * then store in a file.
             * @param e
             */
            @Override
            /**
             * @see javax.awt.event.addActionListener#actionPerformed(javax.awt.event.ActionListener)
             */
            public void actionPerformed(ActionEvent e) {
                String billboardName = createBbName.getText();
                String msg = createBbMsg.getText();
                String info = createBbInfo.getText();
                String url = createBbImgLink.getText();

                String billboardColour = convertColorToHexadecimal(bgColourPreview.getBackground());
                String messageColour = convertColorToHexadecimal(msgColourPreview.getBackground());
                String informationColour = convertColorToHexadecimal(infoColourPreview.getBackground());

                if (!billboardName.contentEquals("")){
                    try(OutputStream out = new FileOutputStream(billboardName+".xml")) {
                        // when there is no input
                        if (info.isEmpty() && url.isEmpty() && msg.isEmpty()) {
                            JOptionPane.showMessageDialog(controlPanel, "Empty input in: \n"
                                            + "1. message\n" + "2. information\n" + "3. image link"
                                    , "Warning", JOptionPane.NO_OPTION);

                        }
                        // When there is input
                        else {
                            // Create an XML string
                            String xml = createXMLString(billboardColour, msg, messageColour, info, informationColour, url);
                            byte[] format = xml.getBytes();
                            out.write(format);
                            out.close();
                            createBbName.setText("");
                            createBbMsg.setText("");
                            createBbInfo.setText("");
                            createBbImgLink.setText("");
                            JOptionPane.showMessageDialog(controlPanel,"Successfully exported into: "+billboardName+".xml"
                                    ,"Information",JOptionPane.NO_OPTION);

                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    JOptionPane.showMessageDialog(controlPanel,"Billboard name cannot be left blank","Warning",JOptionPane.NO_OPTION);
                }

            }
        });


        createBbPreviewButton.addActionListener(new ActionListener() {
            /**
             *Implements a ActionListener for previewButton to show added billBoardData
             * @param e
             */
            @Override
            /**
             * @see javax.awt.event.addActionListener#actionPerformed(javax.awt.event.ActionListener)
             */
            public void actionPerformed(ActionEvent e) {
                // Get information from the fields
                String msg = createBbMsg.getText();
                String imageLink = createBbImgLink.getText();
                String info = createBbInfo.getText();

                Color billboardColour = bgColourPreview.getBackground();
                Color messageColour = msgColourPreview.getBackground();
                Color informationColour = infoColourPreview.getBackground();

                // Create an instance of the billboard viewer
                try {
                    new BillboardViewer(billboardColour, msg, messageColour, info, informationColour, imageLink, false);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    /**
     *This function used to check if the chosen file is xml file.
     * @param fullName
     * @return
     */
    private static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * This function used to make billBoard data filled by user,
     * change to xml form
     * @param billboardColour
     * @param message
     * @param messageColour
     * @param info
     * @param infoColour
     * @param imgData
     * @return
     */
    private static String createXMLString(String billboardColour, String message, String messageColour,
                                          String info, String infoColour, String imgData) {
        // Initial result, open the billboard tag
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard";

        // Check if there is any background colour
        if (billboardColour.length() > 0) {
            // Add background colour to the XML file
            result += " background=\"" + billboardColour + "\"";
        }

        // Close the bracket
        result += ">";

        // Check if there is any message
        if (message.length() > 0) {
            // Add message tag
            result += "\n<message";

            // Check if there is any message colour
            if (messageColour.length() > 0) {
                result += " colour=\"" + messageColour + "\"";
            }

            // Add message
            result += ">" + message + "</message>";
        }

        // Check if there is any image
        if (imgData.length() > 4) {
            // Add message tag
            result += "\n<picture ";

            // Check if it is an URL or Base64
            if (imgData.substring(0,4).contains("http")) {
                // URL
                result += "url=\"" + imgData + "\" />";
            } else {
                result += "data=\"" + imgData + "\" />";
            }
        }

        // Check if there is any information
        if (info.length() > 0) {
            // Add information tag
            result += "\n<information";

            // Check if there is any information colour
            if (infoColour.length() > 0) {
                result += " colour=\"" + infoColour + "\"";
            }

            // Add information
            result += ">" + info + "</information>";
        }

        // Close the billboard tag
        result += "\n</billboard>";

        // Return the result
        return result;
    }

    /**
     * Convert colour into a hexadecimal string
     *
     * @param colour The colour
     * @return The hex value of the colour
     */
    public static String convertColorToHexadecimal(Color colour)
    {
        String hex = String.format( "#%02X%02X%02X", colour.getRed(), colour.getGreen(), colour.getBlue());
        return hex;
    }

    /**
     * setter function to set value for GUI elements and variables
     * @param controlPanelGUI
     */
    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.controlPanel = controlPanelGUI.controlPanel;
        this.createBbName = controlPanelGUI.createBbName;
        this.createBbMsg = controlPanelGUI.createBbMsg;
        this.createBbInfo = controlPanelGUI.createBbInfo;
        this.createBbImgLink = controlPanelGUI.createBbImgLink;
        this.createBbSubmitButton = controlPanelGUI.createBbSubmitButton;
        this.createBbPreviewButton = controlPanelGUI.createBbPreviewButton;
        this.exportToXMLButton =controlPanelGUI.exportToXMLButton;
        this.importXMLButton = controlPanelGUI.importXMLButton;
        this.billboardColourButton = controlPanelGUI.billboardColourButton;
        this.messageColourButton = controlPanelGUI.messageColourButton;
        this.informationColourButton = controlPanelGUI.informationColourButton;
        this.chooseAFileButton = controlPanelGUI.chooseAFileButton;
        this.infoColourPreview = controlPanelGUI.infoColourPreview;
        this.msgColourPreview = controlPanelGUI.messColourPreview;
        this.bgColourPreview = controlPanelGUI.bgColourPreview;
    }
}
