package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
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



    String msgBillboard = "<billboard>\n" +
            "<message>" + "msg" + "</message>\n" +
            "</billboard>";
    String infoBillboard = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard background=\"#0000FF\">\n" +
            " <information colour=\"#00FFFF\">" + "replaceinfo"  + "</information>\n" +
            "</billboard>";
    String picBillboard = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard background=\"#0000FF\">\n" +
            " <picture url=\"" + "data" + "\" />\n" +
            "</billboard>";
    String msgAndinfo = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard background=\"#0000FF\">\n" +
            "<message>" + "msg" + "</message>\n" +
            " <information colour=\"#00FFFF\">" + "replaceinfo" + "</information>\n" +
            "</billboard>";
    String msgAndpic = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard background=\"#0000FF\">\n" +
            "<message>" + "msg" + "</message>\n" +
            " <picture url=\"" + "data" + "\" />\n" +
            "</billboard>";
    String infoAndpic = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard background=\"#0000FF\">\n" +
            " <information colour=\"#00FFFF\">" + "replaceinfo"  + "</information>\n" +
            " <picture url=\"" + "data" + "\" />\n" +
            "</billboard>";
    String allformat = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard background=\"#0000FF\">\n" +
            " <message colour=\"#FFFF00\">" + "msg" + "</message>\n" +
            " <picture url=\"" + "data" + "\" />\n" +
            " <information colour=\"#00FFFF\">" + "replaceinfo" + "</information>\n" +
            "</billboard>";

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
                        //when there is no input
                        if (info.isEmpty() && url.isEmpty() && msg.isEmpty()){
                            JOptionPane.showMessageDialog(controlPanel,"Empty input in: \n"
                                            + "1. message\n" + "2. information\n" + "3. image link"
                                    ,"Warning",JOptionPane.NO_OPTION);

                        }
                        //message, information and url field have value
                        else if (info.length()>0 && url.length()>0 && msg.length()>0){
                            String allXML = allformat.replace("msg",msg).replace("replaceinfo",info).replace("data",url);
                            byte[] format = allXML.getBytes();
                            out.write(format);
                            out.close();
                            createBbName.setText("");
                            createBbMsg.setText("");
                            createBbInfo.setText("");
                            createBbImgLink.setText("");
                            JOptionPane.showMessageDialog(controlPanel,"Exported Successful into: "+billboardName+".xml"
                                    ,"Information",JOptionPane.NO_OPTION);

                        }
                        //message and information field have value
                        else if(msg.length()>0 && info.length()>0 ){ //message and info
                            String msginfoXML = msgAndinfo.replace("msg",msg).replace("replaceinfo",info);
                            byte[] format = msginfoXML.getBytes();
                            out.write(format);
                            out.close();
                            createBbName.setText("");
                            createBbMsg.setText("");
                            createBbInfo.setText("");
                            createBbImgLink.setText("");
                            JOptionPane.showMessageDialog(controlPanel,"Exported Successful into: "+billboardName+".xml"
                                    ,"Information",JOptionPane.NO_OPTION);
                        }
                        //message and url field have value
                        else if(msg.length()>0 && url.length()>0 ){ //message and url
                            String msgurlXML = msgAndpic.replace("msg",msg).replace("data",url);
                            byte[] format = msgurlXML.getBytes();
                            out.write(format);
                            out.close();
                            createBbName.setText("");
                            createBbMsg.setText("");
                            createBbInfo.setText("");
                            createBbImgLink.setText("");
                            JOptionPane.showMessageDialog(controlPanel,"Exported Successful into: "+billboardName+".xml"
                                    ,"Information",JOptionPane.NO_OPTION);
                        }
                        //url and information field have value
                        else if(info.length()>0 && url.length()>0 ){ //info and url
                            String infourlXML = infoAndpic.replace("replaceinfo",info).replace("data",url);
                            byte[] format = infourlXML.getBytes();
                            out.write(format);
                            out.close();
                            createBbName.setText("");
                            createBbMsg.setText("");
                            createBbInfo.setText("");
                            createBbImgLink.setText("");
                            JOptionPane.showMessageDialog(controlPanel,"Exported Successful into: "+billboardName+".xml"
                                    ,"Information",JOptionPane.NO_OPTION);
                        }
                        //only information field have value
                        else if(info.length()>0){ //only info
                            String infoXML = infoBillboard.replace("replaceinfo",info);
                            byte[] format = infoXML.getBytes();
                            out.write(format);
                            out.close();
                            createBbName.setText("");
                            createBbMsg.setText("");
                            createBbInfo.setText("");
                            createBbImgLink.setText("");
                            JOptionPane.showMessageDialog(controlPanel,"Exported Successful into: "+billboardName+".xml"
                                    ,"Information",JOptionPane.NO_OPTION);
                        }
                        //only url field have value
                        else if(url.length()>0){ //only url
                            String urlXML = picBillboard.replace("data",url);
                            byte[] format = urlXML.getBytes();
                            out.write(format);
                            out.close();
                            createBbName.setText("");
                            createBbMsg.setText("");
                            createBbInfo.setText("");
                            createBbImgLink.setText("");
                            JOptionPane.showMessageDialog(controlPanel,"Exported Successful into: "+billboardName+".xml"
                                    ,"Information",JOptionPane.NO_OPTION);
                        }
                        //only message field have value
                        else if (msg.length()>0) { //only message
                            String msgXML = msgBillboard.replace("msg",msg);
                            byte[] format = msgXML.getBytes();
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
                String billboardName = createBbName.getText();
                String msg = createBbMsg.getText();
                String imagelink = createBbImgLink.getText();
                String info = createBbInfo.getText();

                if(imagelink.length()>0){
                try {
                    if(!imagelink.substring(0,4).contentEquals("http")){
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(imagelink)));
                        ImageIcon billboard = new ImageIcon(img);
                        Image image =  billboard.getImage();
                        Image newimg = image.getScaledInstance(240,120, Image.SCALE_SMOOTH);
                        billboard = new ImageIcon(newimg);
                        System.out.println("not base64");

                        JFrame frame1 = new JFrame("preview");
                        JOptionPane.showMessageDialog(null,
                                "Billboard Name: " + billboardName + '\n'+"Message:" + msg + '\n'+"Information: " + info,
                                "Preview",
                                JOptionPane.INFORMATION_MESSAGE,billboard);
                    }
                    else{

                        URL url2 = new URL(imagelink);
                        BufferedImage img1 = ImageIO.read(url2);
                        ImageIcon billboardimage = new ImageIcon(img1);

                        Image image =  billboardimage.getImage();
                        Image newimg = image.getScaledInstance(240,120, Image.SCALE_SMOOTH);
                        billboardimage = new ImageIcon(newimg);

                        JFrame frame2 = new JFrame("preview");
                        JOptionPane.showMessageDialog(null,
                                "Billboard Name: " + billboardName + '\n'+"Message:" + msg+ '\n'+"Information: " + info,
                                "Preview",
                                JOptionPane.INFORMATION_MESSAGE,billboardimage);

                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
