import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class clientGUI {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JTextArea textArea4;
    private JButton submitButton;
    private JTextArea textArea8;
    private JTextArea textArea10;
    private JTextArea textArea13;
    private JTextArea textArea14;
    private JTextArea textArea15;
    private JTextArea textArea16;
    private JTextArea textArea17;
    private JTextArea textArea19;
    private JButton submitButton2;
    private JTextArea textArea21;
    private JTextArea textArea22;
    private JTextArea textArea23;
    private JButton editButton;
    private JButton submitButton1;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable table1;
    private JTable table2;
    private JButton previewButton;
    private JButton deleteButton1;
    private JButton editButton1;
    private JTextArea textArea5;
    String billboardName;
    String msg;
    String url;
    private Integer billboardID;
    private int selectedRow;

    private int rowToEdit;

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

                textArea5.setText(billboardId);
                textArea5.setEditable(false);
                textArea8.setText(billboardName);
                textArea10.setText(billboardMessage);
                textArea13.setText(billboardInformation);
                textArea14.setText(billboardUrl);
                rowToEdit = selectedRow;
                tabbedPane1.setSelectedIndex(2);

            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int billboardId = Integer.valueOf(textArea5.getText());
                String billboardName = textArea8.getText();
                String billboardMessage = textArea10.getText();
                String billboardInformation = textArea13.getText();
                String billboardUrl = textArea14.getText();
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


                textArea8.setText("");
                textArea10.setText("");
                textArea13.setText("");
                textArea14.setText("");
                textArea5.setText("");
                JOptionPane.showMessageDialog(panel1,"Success","message",JOptionPane.NO_OPTION);
                tabbedPane1.setSelectedIndex(0);


            }
        });



        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String billboardName = textArea1.getText();
                    String msg = textArea2.getText();
                    String info = textArea3.getText();
                    String url = textArea4.getText();
                    String requestType = "addBillboard";
                    //MyClass myclass = new MyClass(requestType);
                    //oos.writeObject(myclass);
                    oos.writeUTF(requestType);
                    oos.flush();


                    Add addBillboard = new Add(billboardName,msg,info,url);
                    oos.writeObject(addBillboard);
                    oos.flush();
                    textArea1.setText("");
                    textArea2.setText("");
                    textArea3.setText("");
                    textArea4.setText("");
                    Object[]  data = addBillboard.getVal();

                    if(billboardID != null){
                        System.out.println("Selected id "+billboardID);
                        DefaultTableModel dm = (DefaultTableModel) table2.getModel();
                        System.out.println("row "+selectedRow);
                        dm.addRow(data);
                    }

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
                billboardName = textArea1.getText();
                msg = textArea2.getText();
                url = textArea4.getText();

                try {
                    URL url2 = new URL(url);
                    BufferedImage img = ImageIO.read(url2);
                    ImageIcon icon = new ImageIcon(img);

                    JFrame frame = new JFrame("preview");
                    JOptionPane.showMessageDialog(null,
                            "Billboard Name: " + billboardName + '\n'+"Message:" + msg +'\n'+"Image: "+url,
                            "Preview",
                            JOptionPane.INFORMATION_MESSAGE,icon);

                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }



            }
        });

    }
    public JPanel getRootPanel(){
        return panel1;
    }


}
