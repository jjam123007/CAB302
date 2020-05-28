package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import ControlPanel.SerializeArray;
import User.ClientUser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Base64;

/**
 * @author Jun Chen(n10240977)&Haoze He(n10100351)
 */
public class CreateBillboards implements ControlPanelComponent {
    private ObjectOutputStream oos;
    private JPanel controlPanel;
    private JTextArea createBbName;
    private JTextArea createBbMsg;
    private JTextArea createBbInfo;
    private JTextArea createBbImgLink;
    private JButton createBbSubmitButton;
    private JButton createBbPreviewButton;

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
                    String requestType = "addBillboard";
                    Object[] newTable = {billboardName,msg,info,url};
                    BillboardRequest addBillboard = new BillboardRequest(BillboardRequestType.addBillboard,newTable, ClientUser.getToken());
                    oos.writeObject(addBillboard);
                    oos.flush();
                    JOptionPane.showMessageDialog(controlPanel,"Success","message",JOptionPane.NO_OPTION);
                    BillboardReply message = new BillboardReply("Success");
                    oos.writeObject(message);
                    oos.flush();

                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                createBbName.setText("");
                createBbMsg.setText("");
                createBbInfo.setText("");
                createBbImgLink.setText("");


            }
        });
        createBbPreviewButton.addActionListener(new ActionListener() {
            /**
             *Implements a ActionListener for previewButton to show added data on dialog
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

                try {
                    if(!imagelink.substring(0,4).contentEquals("http")){
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(imagelink)));
                        ImageIcon icon1 = new ImageIcon(img);
                        Image image =  icon1.getImage();
                        Image newimg = image.getScaledInstance(240,120, Image.SCALE_SMOOTH);
                        icon1 = new ImageIcon(newimg);
                        System.out.println("not base64");

                        JFrame frame1 = new JFrame("preview");
                        JOptionPane.showMessageDialog(null,
                                "Billboard Name: " + billboardName + '\n'+"Message:" + msg,
                                "Preview",
                                JOptionPane.INFORMATION_MESSAGE,icon1);
                    }
                    else{

                        URL url2 = new URL(imagelink);
                        BufferedImage img1 = ImageIO.read(url2);
                        ImageIcon icon = new ImageIcon(img1);

                        Image image =  icon.getImage();
                        Image newimg = image.getScaledInstance(240,120, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(newimg);

                        JFrame frame2 = new JFrame("preview");
                        JOptionPane.showMessageDialog(null,
                                "Billboard Name: " + billboardName + '\n'+"Message:" + msg,
                                "Preview",
                                JOptionPane.INFORMATION_MESSAGE,icon);

                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * setter function to set value for GUI elements and variables
     * @param controlPanelGUI
     */
    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.oos = controlPanelGUI.oos;
        this.controlPanel = controlPanelGUI.controlPanel;

        this.createBbName = controlPanelGUI.createBbName;
        this.createBbMsg = controlPanelGUI.createBbMsg;
        this.createBbInfo = controlPanelGUI.createBbInfo;
        this.createBbImgLink = controlPanelGUI.createBbImgLink;
        this.createBbSubmitButton = controlPanelGUI.createBbSubmitButton;
        this.createBbPreviewButton = controlPanelGUI.createBbPreviewButton;
    }
}
