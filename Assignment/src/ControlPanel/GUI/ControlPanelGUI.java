package ControlPanel.GUI;

import ControlPanel.GUI.AdminPane.AdminPane;
import ControlPanel.GUI.AdminPane.RegisterUser;
import ControlPanel.GUI.BillboardsPane.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ControlPanelGUI {
    protected JPanel controlPanel;
    protected JTabbedPane billboardsPane;

    protected JTextArea createBbName;
    protected JTextArea createBbMsg;
    protected JTextArea createBbInfo;
    protected JTextArea createBbImgLink;
    protected JButton createBbSubmitButton;
    protected JButton createBbPreviewButton;

    protected JTextArea editBbName;
    protected JTextArea editBbMsg;
    protected JTextArea editBbInfo;
    protected JTextArea editBbImgLink;
    protected JButton editUpdateButton;
    protected JButton editDeleteButton;
    protected JTextArea editBbID;

    protected JTextArea scheduleBbID;
    protected JTextArea scheduleEndTime;
    protected JTextArea scheduleStartTime;
    protected JTextArea scheduleBbDate;
    protected JButton scheduleSubmitButton;

    protected JTable viewTable;
    protected JButton viewEditButton;
    protected JButton viewDeleteButton;

    protected JTabbedPane menuPane;
    protected JTabbedPane adminPane;
    protected JTextField registerUsernameField;
    protected JPasswordField registerPasswordField;
    protected JPasswordField registerReenterPasswordField;
    protected JCheckBox createBillboardsPerm;
    protected JCheckBox editUsersPerm;
    protected JCheckBox editBillboardsPerm;
    protected JCheckBox scheduleBillboardsPerm;
    protected JLabel registerReplyMessage;
    protected JButton registerUserButton;
    protected int rowToEdit;

    private Socket socket;

    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;

    //Copy the parent ControlPanel variables.
    public ControlPanelGUI(ControlPanelGUI controlPanelGUI){
        this.oos = controlPanelGUI.oos;
        this.ois = controlPanelGUI.ois;

        this.controlPanel = controlPanelGUI.controlPanel;
        this.billboardsPane = controlPanelGUI.billboardsPane;

        this.createBbName = controlPanelGUI.createBbName;
        this.createBbMsg = controlPanelGUI.createBbMsg;
        this.createBbInfo = controlPanelGUI.createBbInfo;
        this.createBbImgLink = controlPanelGUI.createBbImgLink;
        this.createBbSubmitButton = controlPanelGUI.createBbSubmitButton;
        this.createBbPreviewButton = controlPanelGUI.createBbPreviewButton;

        this.editBbName = controlPanelGUI.editBbName;
        this.editBbMsg = controlPanelGUI.editBbMsg;
        this.editBbInfo = controlPanelGUI.editBbInfo;
        this.editBbImgLink = controlPanelGUI.editBbImgLink;
        this.editUsersPerm = controlPanelGUI.editUsersPerm;
        this.editBillboardsPerm = controlPanelGUI.editBillboardsPerm;
        this.editUpdateButton = controlPanelGUI.editUpdateButton;
        this.editDeleteButton = controlPanelGUI.editDeleteButton;
        this.editBbID = controlPanelGUI.editBbID;

        this.scheduleBbID = controlPanelGUI.scheduleBbID;
        this.scheduleEndTime = controlPanelGUI.scheduleEndTime;
        this.scheduleStartTime = controlPanelGUI.scheduleStartTime;
        this.scheduleBbDate = controlPanelGUI.scheduleBbDate;
        this.scheduleSubmitButton = controlPanelGUI.scheduleSubmitButton;

        this.viewTable = controlPanelGUI.viewTable;
        this.viewEditButton = controlPanelGUI.viewEditButton;
        this.viewDeleteButton = controlPanelGUI.viewDeleteButton;

        this.menuPane = controlPanelGUI.menuPane;
        this.adminPane = controlPanelGUI.adminPane;
        this.registerUsernameField = controlPanelGUI.registerUsernameField;
        this.registerPasswordField = controlPanelGUI.registerPasswordField;
        this.registerReenterPasswordField = controlPanelGUI.registerReenterPasswordField;
        this.registerUserButton = controlPanelGUI.registerUserButton;

        this.createBillboardsPerm = controlPanelGUI.createBillboardsPerm;
        this.scheduleBillboardsPerm = controlPanelGUI.scheduleBillboardsPerm;
        this.registerReplyMessage = controlPanelGUI.registerReplyMessage;
        this.rowToEdit = controlPanelGUI.rowToEdit;
    }

    public ControlPanelGUI(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        JPanel root = this.controlPanel;
        JFrame frame = new JFrame();
        frame.setContentPane(root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.oos = oos;
        this.ois = ois;
        initControlPanelComponents();
    }

    private void initControlPanelComponents() throws IOException, ClassNotFoundException {
        new CreateBillboards(this);
        new EditBillboards(this);
        new ScheduleBillboards(this);
        new ViewBillboards(this);

        new AdminPane(this);
    }
}
