package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import BillboardViewer.BillboardViewer;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import ControlPanel.SerializeArray;
import Networking.Request;
import User.ClientUser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Base64;

/**
 * This class used to create and preview a new billBoard
 * @author Jun Chen(n10240977) & Haoze He(n10100351) & William Tran (n10306234)
 */
public class CreateBillboards implements ControlPanelComponent {

    private JPanel controlPanel;
    private JTextArea createBbName;
    private JTextArea createBbMsg;
    private JTextArea createBbInfo;
    private JTextArea createBbImgLink;
    private JButton createBbSubmitButton;
    private JButton createBbPreviewButton;
    public JButton exportToXMLButton;
    public JTabbedPane billboardsPane;

    /**
     *
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public CreateBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);
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
                    String xml = createXMLString("", msg, "", info, "", url);


                    Object[] newTable = {billboardName,msg,info,url,xml};
                    BillboardRequest addBillboard = new BillboardRequest(BillboardRequestType.addBillboard, newTable, ClientUser.getToken());
                    //read the reply from the server
                    BillboardReply messageObject = (BillboardReply) addBillboard.getOIS().readObject();
                    addBillboard.closeConnection();
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
                createBbName.setText("");
                createBbMsg.setText("");
                createBbInfo.setText("");
                createBbImgLink.setText("");


            }
        });
        exportToXMLButton.addActionListener(new ActionListener() {
            /**
             * Implements a ActionListener for xmlButton to added billBoard data from xml form file.
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
                            String xml = createXMLString("", msg, "", info, "", url);
                            byte[] format = xml.getBytes();
                            out.write(format);
                            out.close();
                            createBbName.setText("");
                            createBbMsg.setText("");
                            createBbInfo.setText("");
                            createBbImgLink.setText("");
                            JOptionPane.showMessageDialog(controlPanel,"Exported Successful into: "+billboardName+".xml"
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
             *Implements a ActionListener for previewButton to show added data on dialogBox
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

                // Create an instance of the billboard viewer
                try {
                    new BillboardViewer(Color.decode("#ffffff"), msg, null, info, null, imageLink, false);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
    }


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
    }
}
