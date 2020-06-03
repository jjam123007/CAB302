
package ControlPanel.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

/**
 * @author Jun Chen (n10240977) & Haoze He (n10100351) & Nikolai Taufao (n10481087) & William Tran (n10306234)
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
    public JLabel editBbID;

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
    public JLabel toEditRow;
    public JButton exportToXMLButton;
    public JPanel EditJPanel;
    private JPanel CreateJPanel;
    private JPanel ViewJPanel;
    private JPanel ScheduleJPanel;
    public JButton importXMLButton;
    public JCheckBox detailsCreateBillboardsPerm;
    public JLabel accountUsernameLabel;
    public JButton updatePermissionsButton;
    public JCheckBox detailsEditBillboardsPerm;
    public JCheckBox detailsScheduleBillboardsPerm;
    public JCheckBox detailsEditUsersPerm;
    public JButton billboardColourButton;
    public JButton messageColourButton;
    public JButton informationColourButton;
    public JButton chooseImageButton;
    public JPanel infoColourPreview;
    public JPanel messColourPreview;
    public JPanel bgColourPreview;
    public JButton editBbColourButton;
    public JButton editMessageColourButton;
    public JButton editInfoColourButton;
    public JButton editChooseImageButton;
    public JButton editPreviewButton;
    public JPanel editBbColourPreview;
    public JPanel editMessageColourPreview;
    public JPanel editInfoColourPreview;
    public JButton viewXMLExportButton;
    public JButton addScheduleButton;
    public JTable calenderView;
    public JButton viewScheduleButton;
    public JButton viewAllSchedulesButton;
    public JButton deleteScheduleButton;
    public JButton editCancelButton;
    public JLabel RowToEdit;

    public int rowToEdit;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public static JFrame frame;

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
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(800,500);
        initControlPanelComponents();
        setupClosingConfirmationDialog();
    }

    private void setupClosingConfirmationDialog() {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure to exit the program?", "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    frame.dispose();
                } else {

                }
            }
        });
    }

    private void initControlPanelComponents() throws IOException, ClassNotFoundException, SQLException {
        new MenuPane(this);
    }
}
