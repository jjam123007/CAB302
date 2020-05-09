import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectStreamException;
import java.sql.*;
import java.util.ArrayList;

public class App {

    private JTable table1;
    private JPanel RootPanel;
    private JTabbedPane tabbedPane2;
    private JButton button1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JButton deleteButton;
    private JButton updateButton;
    private Integer selectedPersonID;
    private int selectedRow;

    private Object [][] data;

    public App() throws SQLException {
        createTable();

        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    System.out.println("here");
                    int i = table1.getSelectedRow();
                    if (i >= 0) {
                        selectedPersonID = Integer.parseInt(table1.getValueAt(table1.getSelectedRow(),0).toString());
                        selectedRow = table1.getSelectedRow();
                    }
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = textArea1.getText();
                String query2 = textArea2.getText();
                String query3 = textArea3.getText();

                try {
                    Statement statement = Connect_db.getConnection().createStatement();
                    statement.executeQuery("insert into test values("+query+",'"+query2+"',"+query3+")");
                    statement.close();
                    JOptionPane.showMessageDialog(RootPanel,"Success","message",JOptionPane.NO_OPTION);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(RootPanel,"ERROR","message",JOptionPane.YES_OPTION);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedPersonID != null){
                    System.out.println("Selected id "+selectedPersonID);
                    try {
                        Statement statement = Connect_db.getConnection().createStatement();
                        //statement.executeQuery("delete from test where personId="+selectedPersonID);
                        DefaultTableModel dm = (DefaultTableModel) table1.getModel();
                        System.out.println("row "+selectedRow);
                        dm.removeRow(selectedRow);

                        statement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
    }
    public JPanel getRootPanel(){
        return RootPanel;
    }

    public void loadData() throws SQLException {
        Statement statement = Connect_db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM test");

        int rowcount = 0;

        if (resultSet.last()) {
            rowcount = resultSet.getRow();
            resultSet.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
        }
        data =  new Object[rowcount][3];

        for (int i=0; i<rowcount; i++){
            resultSet.next();
            String personID =Integer.toString(resultSet.getInt(1));
            String personName =resultSet.getString(2);
            String someNumber=Float.toString(resultSet.getFloat(3));
            String[] myString= {personID,personName,someNumber};
            data[i]=myString;
        }
        statement.close();
    }
    public void createTable() throws SQLException {
        loadData();
        table1.setModel(new DefaultTableModel(
                data,
                new String[]{"personID","personName","someNumber"}
        ));
    };





}
