package ControlPanel.GUI;

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
    protected JButton createSubmitButton;
    protected JButton createPreviewButton;

    protected JTextArea editBbName;
    protected JTextArea editBbMsg;
    protected JTextArea editBbInfo;
    protected JTextArea editBbImgLink;
    protected JCheckBox editUsersAdminCheckBox;
    protected JCheckBox editBillboardsCheckBox;
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
    protected JTextField textField1;
    protected JPasswordField passwordField1;
    protected JPasswordField passwordField2;
    protected JCheckBox createBillboardsCheckBox;
    protected JCheckBox scheduleBillboardsCheckBox;
    protected JLabel errorMessage;
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
        this.createSubmitButton = controlPanelGUI.createSubmitButton;
        this.createPreviewButton = controlPanelGUI.createPreviewButton;

        this.editBbName = controlPanelGUI.editBbName;
        this.editBbMsg = controlPanelGUI.editBbMsg;
        this.editBbInfo = controlPanelGUI.editBbInfo;
        this.editBbImgLink = controlPanelGUI.editBbImgLink;
        this.editUsersAdminCheckBox = controlPanelGUI.editUsersAdminCheckBox;
        this.editBillboardsCheckBox = controlPanelGUI.editBillboardsCheckBox;
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
        this.textField1 = controlPanelGUI.textField1;
        this.passwordField1 = controlPanelGUI.passwordField1;
        this.passwordField2 = controlPanelGUI.passwordField2;
        this.createBillboardsCheckBox = controlPanelGUI.createBillboardsCheckBox;
        this.scheduleBillboardsCheckBox = controlPanelGUI.scheduleBillboardsCheckBox;
        this.errorMessage = controlPanelGUI.errorMessage;
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
        this.rowToEdit = 2;
        initControlPanelComponents();
    }

    private void initControlPanelComponents() throws IOException, ClassNotFoundException {
        CreateBillboards createBillboards = new CreateBillboards(this);
        EditBillboards editBillboards = new EditBillboards(this);
        ScheduleBillboards scheduleBillboards = new ScheduleBillboards(this);
        ViewBillboards viewBillboards =  new ViewBillboards(this);
    }
}
