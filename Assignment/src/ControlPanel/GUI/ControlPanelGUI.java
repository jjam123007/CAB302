
package ControlPanel.GUI;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

/**
 * This class contains all of the java swing components that form the control panel GUI.
 */
public class ControlPanelGUI {
    public JPanel controlPanel;
    public JTabbedPane billboardsPane;

    public JTextArea createBbName;
    public JTextArea createBbMsg;
    public JTextArea createBbInfo;
    public JTextArea createBbImgLink;
    public JButton createBbSubmitButton;
    public JButton createBbPreviewButton;

    public JTextArea editBbName;
    public JTextArea editBbMsg;
    public JTextArea editBbInfo;
    public JTextArea editBbImgLink;
    public JButton editUpdateButton;
    public JTextArea editBbID;

    public JTextArea scheduleBbID;
    public JTextArea scheduleEndTime;
    public JTextArea scheduleStartTime;
    public JTextArea scheduleBbDate;
    public JButton scheduleSubmitButton;

    public JTable viewTable;
    public JButton viewEditButton;
    public JButton viewDeleteButton;

    public JTabbedPane menuPane;
    public JTabbedPane adminPane;
    public JTextField registerUsernameField;
    public JPasswordField registerPasswordField;
    public JPasswordField registerReenterPasswordField;
    public JCheckBox createBillboardsPerm;
    public JCheckBox editUsersPerm;
    public JCheckBox editBillboardsPerm;
    public JCheckBox scheduleBillboardsPerm;

    public JLabel registerReplyMessage;
    public JButton registerUserButton;
    public JScrollPane usersScrollPane;
    public JPanel editUsersPanel;
    public JTable usersTable;
    public JCheckBox editUsersAdminPermEdit;
    public JCheckBox createBillboardsPermEdit;
    public JCheckBox scheduleBillboardsPermEdit;
    public JCheckBox editBillboardsPermEdit;

    public JPasswordField editReenterPasswordField;
    public JPasswordField editPasswordField;
    public JButton removeUserButton;
    public JButton changePasswordButton;
    public JButton updateUserListButton;
    public JButton changePermissionsButton;
    public JTabbedPane tabbedPane1;
    public  JButton clientChangePasswordButton;
    public  JPasswordField clientReenterPasswordField;
    public  JPasswordField clientNewPasswordField;
    public JButton logoutButton;
    public JTextArea toEditRow;
    public JButton exportToXMLButton;
    public JPanel EditJPanel;
    private JPanel CreateJPanel;
    private JPanel ViewJPanel;
    private JPanel ScheduleJPanel;
    public JLabel RowToEdit;

    public int rowToEdit;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    protected JFrame frame;

    /**
     * Create the control panel GUI display it on screen.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ControlPanelGUI() throws IOException, ClassNotFoundException, SQLException {
        JPanel root = this.controlPanel;
        frame = new JFrame();
        frame.setContentPane(root);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        initControlPanelComponents();
    }

    private void initControlPanelComponents() throws IOException, ClassNotFoundException, SQLException {
        new MenuPane(this);
    }
}
