package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import UserManagement.ClientUser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Base64;

public class CreateBillboards implements ControlPanelComponent {
    private ObjectOutputStream oos;
    private JPanel controlPanel;
    private JTextArea createBbName;
    private JTextArea createBbMsg;
    private JTextArea createBbInfo;
    private JTextArea createBbImgLink;
    private JButton createBbSubmitButton;
    private JButton createBbPreviewButton;

    public CreateBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        setControlPanelComponents(controlPanelGUI);
        createBbSubmitButton.addActionListener(new ActionListener() {
            @Override
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

                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        createBbPreviewButton.addActionListener(new ActionListener() {
            @Override
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
