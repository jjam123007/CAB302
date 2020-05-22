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

public class clientGUI {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextArea createBillboardName;
    private JTextArea createMsg;
    private JTextArea createInfo;
    private JTextArea textArea4;
    private JButton submitButton;
    private JTextArea editBbName;
    private JTextArea editMsg;
    private JTextArea editInfo;
    private JTextArea editLink;
    private JTextArea scheduleID;
    private JTextArea scheduleEndTime;
    private JTextArea scheduleStartTime;
    private JTextArea sceduledDate;
    private JButton scheduleSubmitButton;
    private JTextArea textArea21;
    private JTextArea textArea22;
    private JTextArea textArea23;
    private JButton createEditButton;
    private JButton createSubmitButton;
    private JButton editUpdateButton;
    private JButton editDeleteButton;
    private JTable table1;
    private JTable table2;
    private JButton previewButton;
    private JButton deleteButton1;
    private JButton editButton1;
    private JTextArea EditBbID;
    private JTextArea createImgLink;
    String billboardName;
    String msg;
    String imagelink;
    private Integer billboardID;
    private int selectedRow;

    private int rowToEdit;

    // User permissions, based on data retrieved from the database
    private boolean isCreator = true;
    private boolean isEditor = true;
    private boolean isScheduler = true;
    private boolean isAdmin = true;
    ////---- To be updated ---////

    public clientGUI() throws IOException, ClassNotFoundException {
//        createTable();
        Socket socket = new Socket("localhost", 3310);

        OutputStream os = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(inputStream);


        String requestType = "showTable";
        //MyClass myclass = new MyClass(requestType);
        //oos.writeObject(myclass);
        //oos.flush();
        oos.writeUTF(requestType);
        oos.flush();


        dataArray tableData = (dataArray) ois.readObject();
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
        deleteButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String requestType = "delete";
                    oos.writeUTF(requestType);
                    oos.flush();
                    Delete deleteTable = new Delete(billboardID);
                    oos.writeObject(deleteTable);
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

        editButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String billboardId = table2.getModel().getValueAt(selectedRow,0).toString();
                String billboardName = (String)table2.getModel().getValueAt(selectedRow,1);
                String billboardMessage = (String)table2.getModel().getValueAt(selectedRow,2);
                String billboardInformation = (String)table2.getModel().getValueAt(selectedRow,3);
                String billboardUrl = (String)table2.getModel().getValueAt(selectedRow,4);

                EditBbID.setText(billboardId);
                EditBbID.setEditable(false);
                editBbName.setText(billboardName);
                editMsg.setText(billboardMessage);
                editInfo.setText(billboardInformation);
                editLink.setText(billboardUrl);
                rowToEdit = selectedRow;
                tabbedPane1.setSelectedIndex(2);

            }
        });
        editUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int billboardId = Integer.valueOf(EditBbID.getText());
                String billboardName = editBbName.getText();
                String billboardMessage = editMsg.getText();
                String billboardInformation = editInfo.getText();
                String billboardUrl = editLink.getText();
                System.out.println("Data Updated");
                System.out.println(billboardName+" "+billboardMessage+" "+billboardInformation+" "+billboardUrl);

                String requestType = "editTable";
                try {
                    oos.writeUTF(requestType);
                    oos.flush();
                    Edit editData = new Edit(billboardId,billboardName,billboardMessage,billboardInformation,billboardUrl);
                    oos.writeObject(editData);
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
                editMsg.setText("");
                editInfo.setText("");
                editLink.setText("");
                EditBbID.setText("");
                JOptionPane.showMessageDialog(panel1,"Success","message",JOptionPane.NO_OPTION);
                tabbedPane1.setSelectedIndex(0);


            }
        });



        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String billboardName = createBillboardName.getText();
                    String msg = createMsg.getText();
                    String info = createInfo.getText();
                    String url = textArea4.getText();
                    String requestType = "addBillboard";
                    //MyClass myclass = new MyClass(requestType);
                    //oos.writeObject(myclass);
                    oos.writeUTF(requestType);
                    oos.flush();


                    Add addBillboard = new Add(billboardName,msg,info,url);
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


        previewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                billboardName = createBillboardName.getText();
                msg = createMsg.getText();
                imagelink = createImgLink.getText();

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

        scheduleSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    String billboardId = scheduleID.getText();
                    String scheduledDate = sceduledDate.getText();
                    String startTime = scheduleStartTime.getText();
                    String endTime = scheduleEndTime.getText();
                    String requestType = "addView";
                    //MyClass myclass = new MyClass(requestType);
                    //oos.writeObject(myclass);
                    oos.writeUTF(requestType);
                    oos.flush();


                    Add addview = new Add(billboardId,scheduledDate,startTime,endTime);
                    oos.writeObject(addview);
                    oos.flush();

                    scheduleID.setText("");
                    sceduledDate.setText("");
                    scheduleStartTime.setText("");
                    scheduleEndTime.setText("");


                    JOptionPane.showMessageDialog(panel1,"Success","message",JOptionPane.NO_OPTION);
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }



            }
        });
    }
    public JPanel getRootPanel(){
        if (!isCreator) {
            tabbedPane1.setEnabledAt(1, false);
        }
        if (!isEditor) {
            tabbedPane1.setEnabledAt(2, false);
        }
        if (!isScheduler) {
            tabbedPane1.setEnabledAt(3, false);
        }
        if (!isAdmin) {
            tabbedPane1.setEnabledAt(4, false);
        }
        return panel1;
    }


}
