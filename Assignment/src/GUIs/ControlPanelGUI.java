package GUIs;

import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Base64;

public class ControlPanelGUI {
    private JPanel panel1;
    private JTabbedPane billboardsPane;
    private JTextArea createBbName;
    private JTextArea createBbMsg;
    private JTextArea createBbInfo;
    private JTextArea textArea4;
    private JButton createSubmitButton;
    private JTextArea editBbName;
    private JTextArea editBbMsg;
    private JTextArea editBbInfo;
    private JTextArea editBbImgLink;
    private JTextArea scheduleBbID;
    private JTextArea scheduleEndTIme;
    private JTextArea scheduleStartTime;
    private JTextArea scheduleBbDate;
    private JButton submitButton2;
    private JButton editUpdateButton;
    private JButton editDeleteButton;
    private JTable table2;
    private JButton createPreviewButton;
    private JButton viewDeleteButton;
    private JButton viewEditButton;
    private JTextArea editBbID;
    private JTextArea createBbImgLink;
    private JTabbedPane menuPane;
    private JTabbedPane adminPane;
    private JButton registerUserButton;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JCheckBox createBillboardsCheckBox1;
    private JCheckBox scheduleBillboardsCheckBox2;
    private JCheckBox editUsersAdminCheckBox;
    private JCheckBox editBillboardsCheckBox2;
    private JLabel errorMessage;
    String billboardName;
    String msg;
    String imagelink;
    private Integer billboardID;
    private int selectedRow;
    private int rowToEdit;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public JPanel getRootPanel(){
        return panel1;
    }

    public ControlPanelGUI(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {

        panel1.setVisible(true);
        this.oos = oos;
        this.ois = ois;

        System.out.println("hello34");
        BillboardRequest request = new BillboardRequest(BillboardRequestType.showTable,null,"");
        this.oos.writeObject(request);
        this.oos.flush();
        System.out.println("hello345");
        SerialDataArray tableData = (SerialDataArray) ois.readObject();
        Object[][]  data = tableData.getData();
        
        table2.setModel(new DefaultTableModel(
                data,
                new String[]{"BillboardID","Billboard Name","Information","Message", "Url", "Scheduled Date", "Start time", "End time"}
        ));

        table2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    int i = table2.getSelectedRow();
                    if (i >= 0) {
                        billboardID = Integer.parseInt(table2.getValueAt(table2.getSelectedRow(),0).toString());
                        selectedRow = table2.getSelectedRow();
                    }
                }
            }
        });
        table2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });
        table2.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int billboardId = Integer.valueOf(table2.getModel().getValueAt(row,0).toString());
            }
        });

        viewDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object[] id = {billboardID};
                    BillboardRequest delete = new BillboardRequest(BillboardRequestType.delete,id,"");
                    oos.writeObject(delete);
                    oos.flush();

                    if(billboardID != null){
                        System.out.println("Selected id "+billboardID);
                        DefaultTableModel dm = (DefaultTableModel) table2.getModel();
                        System.out.println("row "+selectedRow);
                        dm.removeRow(selectedRow);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });

        viewEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String billboardId = table2.getModel().getValueAt(selectedRow,0).toString();
                String billboardName = (String)table2.getModel().getValueAt(selectedRow,1);
                String billboardMessage = (String)table2.getModel().getValueAt(selectedRow,2);
                String billboardInformation = (String)table2.getModel().getValueAt(selectedRow,3);
                String billboardUrl = (String)table2.getModel().getValueAt(selectedRow,4);

                editBbID.setText(billboardId);
                editBbID.setEditable(false);
                editBbName.setText(billboardName);
                editBbMsg.setText(billboardMessage);
                editBbInfo.setText(billboardInformation);
                editBbImgLink.setText(billboardUrl);
                rowToEdit = selectedRow;
                billboardsPane.setSelectedIndex(2);

            }
        });
        editUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int billboardId = Integer.valueOf(editBbID.getText());
                String billboardName = editBbName.getText();
                String billboardMessage = editBbMsg.getText();
                String billboardInformation = editBbInfo.getText();
                String billboardUrl = editBbImgLink.getText();
                System.out.println("Data Updated");
                System.out.println(billboardName+" "+billboardMessage+" "+billboardInformation+" "+billboardUrl);

                try {
                    Object[] newTable = {billboardId,billboardName,billboardMessage,billboardInformation,billboardUrl};
                    BillboardRequest edit = new BillboardRequest(BillboardRequestType.edit, newTable,"");
                    oos.writeObject(edit);
                    oos.flush();

                    table2.getModel().setValueAt(billboardId,rowToEdit,0);
                    table2.getModel().setValueAt(billboardName,rowToEdit,1);
                    table2.getModel().setValueAt(billboardMessage,rowToEdit,2);
                    table2.getModel().setValueAt(billboardInformation,rowToEdit,3);
                    table2.getModel().setValueAt(billboardUrl,rowToEdit,4);
                    rowToEdit = -1;

                } catch (IOException ex) {
                    ex.printStackTrace();
                }


                editBbName.setText("");
                editBbMsg.setText("");
                editBbInfo.setText("");
                editBbImgLink.setText("");
                editBbID.setText("");
                JOptionPane.showMessageDialog(panel1,"Success","message",JOptionPane.NO_OPTION);
                billboardsPane.setSelectedIndex(0);


            }
        });

        createSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String billboardName = createBbName.getText();
                    String msg = createBbMsg.getText();
                    String info = createBbInfo.getText();
                    String url = createBbImgLink.getText();
                    String requestType = "addBillboard";
                    //MyClass myclass = new MyClass(requestType);
                    //oos.writeObject(myclass);
                    //oos.writeUTF(requestType);
                    //oos.flush();

                    Object[] newTable = {billboardName,msg,info,url};
                    BillboardRequest addBillboard = new BillboardRequest(BillboardRequestType.addBillboard,newTable,"");
                    oos.writeObject(addBillboard);
                    oos.flush();


                    JOptionPane.showMessageDialog(panel1,"Success","message",JOptionPane.NO_OPTION);
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        //ois.close();
        //oos.close();
        //socket.close();


        createPreviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                billboardName = createBbName.getText();
                msg = createBbMsg.getText();
                imagelink = createBbImgLink.getText();

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

        submitButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    String billboardId = scheduleBbID.getText();
                    String scheduledDate = scheduleBbDate.getText();
                    String startTime = scheduleStartTime.getText();
                    String endTime = scheduleEndTIme.getText();
                    String requestType = "addView";
                    Object[] submitData = {billboardId, scheduledDate,startTime,endTime,requestType};

                    BillboardRequest addview = new BillboardRequest(BillboardRequestType.addView, submitData, "");
                    oos.writeObject(addview);
                    oos.flush();


                    JOptionPane.showMessageDialog(panel1,"Success","message",JOptionPane.NO_OPTION);
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setRegisterUsersTab(){

    }


}
