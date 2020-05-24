package ControlPanel.GUI;

import ControlPanel.GUI.AdminPane.AdminPane;
import ControlPanel.GUI.AdminPane.RegisterUser;
import ControlPanel.GUI.BillboardsPane.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

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
    public JButton editDeleteButton;
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
    private JButton updateUserListButton;
    public int rowToEdit;

    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    public ControlPanelGUI(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        JPanel root = this.controlPanel;
        JFrame frame = new JFrame();
        frame.setContentPane(root);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.oos = oos;
        this.ois = ois;
        initControlPanelComponents();
    }

    private void initControlPanelComponents() throws IOException, ClassNotFoundException {
        new MenuPane(this);
    }
}
