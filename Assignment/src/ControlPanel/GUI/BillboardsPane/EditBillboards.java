package ControlPanel.GUI.BillboardsPane;

import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import BillboardViewer.BillboardViewer;
import ControlPanel.GUI.ControlPanelComponent;
import ControlPanel.GUI.ControlPanelGUI;
import User.ClientUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;


/**
 *This class used to edit exist billBoards
 * @author Haoze He(n10100351) & William Tran (n10306234)
 */
public class EditBillboards implements ControlPanelComponent {

    private JPanel controlPanel;
    public JTabbedPane billboardsPane;
    public JTextArea editBbName;
    public JTextArea editBbMsg;
    public JTextArea editBbInfo;
    public JTextArea editBbImgLink;
    public JButton editUpdateButton;
    public JLabel editBbID;
    public JTable viewTable;
    public JLabel toEditRow;
    public JPanel EditJPanel;
    public JButton editBbColourButton;
    public JButton editMessageColourButton;
    public JButton editInfoColourButton;
    public JButton editChooseImageButton;
    public JButton editPreviewButton;
    public JButton editCancelButton;
    public JPanel editBbColourPreview;
    public JPanel editMessageColourPreview;
    public JPanel editInfoColourPreview;


    public int rowToEdit;
    private static String imageLink;

    /**
     *
     * @param controlPanelGUI
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public EditBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        super();
        setControlPanelComponents(controlPanelGUI);
        editUpdateButton.addActionListener(new ActionListener() {
            @Override
            /**
             * Implements a ActionListener for updateButton to update data is changed in view interface
             * @param e
             * @see javax.awt.event.addActionListener#actionPerformed(javax.awt.event.ActionListener)
             */
            public void actionPerformed(ActionEvent e) {
                // Get inputs from text fields
                int viewId = Integer.valueOf(editBbID.getText());
                String billboardName = editBbName.getText();
                String billboardMessage = editBbMsg.getText();
                String billboardInformation = editBbInfo.getText();
                String billboardUrl = getImage();
                String billboardColour = convertColorToHexadecimal(editBbColourPreview.getBackground());
                String messageColour = convertColorToHexadecimal(editMessageColourPreview.getBackground());
                String informationColour = convertColorToHexadecimal(editInfoColourPreview.getBackground());
                rowToEdit = Integer.valueOf(toEditRow.getText());
                String xml = createXMLString(billboardColour, billboardMessage, messageColour, billboardInformation,
                        informationColour, billboardUrl);
                try {
                    //sends the billboard request to server
                    Object[] newTable = {viewId,billboardName,billboardMessage,billboardInformation,billboardUrl,xml};
                    BillboardRequest edit = new BillboardRequest(BillboardRequestType.edit, newTable, ClientUser.getToken());
                    //get the values at selected row
                    viewTable.getModel().setValueAt(viewId,rowToEdit,0);
                    viewTable.getModel().setValueAt(billboardName,rowToEdit,1);
                    viewTable.getModel().setValueAt(billboardMessage,rowToEdit,2);
                    viewTable.getModel().setValueAt(billboardInformation,rowToEdit,3);
                    viewTable.getModel().setValueAt(billboardUrl,rowToEdit,4);
                    rowToEdit = -1;

                    // Read the reply from the server
                    BillboardReply messageObject = (BillboardReply) edit.getOIS().readObject();
                    edit.closeConnection();
                    String message = messageObject.getMessage();

                    // Reset and disable fields and buttons
                    editBbName.setText("");
                    editBbMsg.setText("");
                    editBbInfo.setText("");
                    editBbImgLink.setText("");
                    editBbID.setText("");
                    toEditRow.setText("CHOOSE A BILLBOARD FROM VIEW TAB TO BEGIN EDITING");
                    editBbID.setText("CHOOSE A BILLBOARD FROM VIEW TAB TO BEGIN EDITING");

                    // Change tab and enable edit fields and buttons
                    editBbName.setEnabled(false);
                    editBbMsg.setEnabled(false);
                    editBbInfo.setEnabled(false);
                    editBbImgLink.setEnabled(false);
                    editBbColourButton.setEnabled(false);
                    editMessageColourButton.setEnabled(false);
                    editInfoColourButton.setEnabled(false);
                    editPreviewButton.setEnabled(false);
                    editUpdateButton.setEnabled(false);
                    editChooseImageButton.setEnabled(false);
                    editCancelButton.setEnabled(false);

                    billboardsPane.setSelectedIndex(0);

                    JOptionPane.showMessageDialog(controlPanel,message,"Message",JOptionPane.NO_OPTION);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        /**
         * Cancel all the changes have been made and disable the fields
         */
        editCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(null,
                        "Are you sure to cancel all the changes?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (confirmation == JOptionPane.OK_OPTION) {
                    // Reset and disable fields and buttons
                    editBbName.setText("");
                    editBbMsg.setText("");
                    editBbInfo.setText("");
                    editBbImgLink.setText("");
                    editBbID.setText("");
                    toEditRow.setText("CHOOSE A BILLBOARD FROM VIEW TAB TO BEGIN EDITING");
                    editBbID.setText("CHOOSE A BILLBOARD FROM VIEW TAB TO BEGIN EDITING");

                    // Change tab and enable edit fields and buttons
                    editBbName.setEnabled(false);
                    editBbMsg.setEnabled(false);
                    editBbInfo.setEnabled(false);
                    editBbImgLink.setEnabled(false);
                    editBbColourButton.setEnabled(false);
                    editMessageColourButton.setEnabled(false);
                    editInfoColourButton.setEnabled(false);
                    editPreviewButton.setEnabled(false);
                    editUpdateButton.setEnabled(false);
                    editChooseImageButton.setEnabled(false);
                    editCancelButton.setEnabled(false);
                    billboardsPane.setSelectedIndex(0);
                }
            }
        });

        /**
         * Add action listeners to choose the colour for the billboard, message and information
         */
        editBbColourButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create a colour chooser dialog
                    Color newColor = JColorChooser.showDialog(
                            ControlPanelGUI.frame,
                            "Choose Background Color",
                            editBbColourPreview.getBackground());

                    // Set the preview colour
                    if (newColor != null) {
                        editBbColourPreview.setBackground(newColor);
                    }
                }
            }
        );

        editMessageColourButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  // Create a colour chooser dialog
                  Color newColor = JColorChooser.showDialog(
                          ControlPanelGUI.frame,
                          "Choose Message Color",
                          editMessageColourPreview.getBackground());

                  // Set the preview colour
                  if (newColor != null) {
                      editMessageColourPreview.setBackground(newColor);
                  }
              }
          }
        );

        editInfoColourButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  // Create a colour chooser dialog
                  Color newColor = JColorChooser.showDialog(
                          ControlPanelGUI.frame,
                          "Choose Information Color",
                          editInfoColourPreview.getBackground());

                  // Set the preview colour
                  if (newColor != null) {
                      editInfoColourPreview.setBackground(newColor);
                  }
              }
          }
        );

        /**
         * Add action listeners to choose an image from a file
         */
        editChooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Create a file chooser
                    final JFileChooser fileChooser = new JFileChooser("./");
                    fileChooser.showOpenDialog(null);
                    File imageFile = fileChooser.getSelectedFile();
                    String fileName = imageFile.getAbsolutePath();
                    String fileExtension = getFileExtension(fileName);
                    //check if its xml file
                    if (fileExtension.contentEquals("bmp") || fileExtension.contentEquals("jpg")
                            || fileExtension.contentEquals("jpeg") || fileExtension.contentEquals("png")) {
                        imageLink = encodeFileToBase64Binary(imageFile);
                        editBbImgLink.setText("(An image file has been chosen. Leave this field blank to not submitting it!)");
                    } else {
                        // If it is not an image file, show a dialog message
                        JOptionPane.showMessageDialog(controlPanel,"Only BMP, JPEG or PNG format is allowed.",
                                "Error", JOptionPane.NO_OPTION);
                    }
                } catch (NullPointerException error) {
                    System.out.println("Nothing was provided as an image import.");
                } catch (Exception ex) {
                    // If an error happens, show a dialog message
                    JOptionPane.showMessageDialog(controlPanel,"An error occurred. Cannot import image.",
                            "Error", JOptionPane.NO_OPTION);
                }
            }
        });

        editPreviewButton.addActionListener(new ActionListener() {
            @Override
            /**
             *Implements a ActionListener for preview button to show added billboard data
             * @param e
             * @see javax.awt.event.addActionListener#actionPerformed(javax.awt.event.ActionListener)
             */
            public void actionPerformed(ActionEvent e) {
                // Get information from the fields
                String msg = editBbMsg.getText();
                String info = editBbInfo.getText();
                String image = getImage();

                Color billboardColour = editBbColourPreview.getBackground();
                Color messageColour = editMessageColourPreview.getBackground();
                Color informationColour = editInfoColourPreview.getBackground();

                // Create an instance of the billboard viewer
                try {
                    new BillboardViewer(billboardColour, msg, messageColour, info, informationColour, image, false);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    /**
     *This function used to check if the chosen file is xml file.
     * @param fullName
     * @return
     */
    private static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * This function used to make billBoard data filled by user,
     * change to xml form
     * @param billboardColour
     * @param message
     * @param messageColour
     * @param info
     * @param infoColour
     * @param imgData
     * @return
     */
    private static String createXMLString(String billboardColour, String message, String messageColour,
                                          String info, String infoColour, String imgData) {
        // Initial result, open the billboard tag
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard";

        // Check if there is any background colour
        if (billboardColour.length() > 0) {
            // Add background colour to the XML file
            result += " background=\"" + billboardColour + "\"";
        }

        // Close the bracket
        result += ">";

        // Check if there is any message
        if (message.length() > 0) {
            // Add message tag
            result += "\n<message";

            // Check if there is any message colour
            if (messageColour.length() > 0) {
                result += " colour=\"" + messageColour + "\"";
            }

            // Add message
            result += ">" + message + "</message>";
        }

        // Check if there is any image
        if (imgData.length() > 4) {
            // Add message tag
            result += "\n<picture ";

            // Check if it is an URL or Base64
            if (imgData.substring(0,4).contains("http")) {
                // URL
                result += "url=\"" + imgData + "\" />";
            } else {
                result += "data=\"" + imgData + "\" />";
            }
        }

        // Check if there is any information
        if (info.length() > 0) {
            // Add information tag
            result += "\n<information";

            // Check if there is any information colour
            if (infoColour.length() > 0) {
                result += " colour=\"" + infoColour + "\"";
            }

            // Add information
            result += ">" + info + "</information>";
        }

        // Close the billboard tag
        result += "\n</billboard>";

        // Return the result
        return result;
    }

    /**
     * Convert colour into a hexadecimal string
     *
     * @param colour The colour
     * @return The hex value of the colour
     */
    public static String convertColorToHexadecimal(Color colour)
    {
        String hex = String.format( "#%02X%02X%02X", colour.getRed(), colour.getGreen(), colour.getBlue());
        return hex;
    }

    /**
     * Convert image file to base64 encoded string
     * @param file
     * @return base64 encoded string
     * @throws Exception if the file cannot be read
     */
    private static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Check if an image string is URL or Base64
     *
     * @return the image string
     */
    private String getImage() {
        if (editBbImgLink.getText().length() < 5) {
            return "";
        } else if (editBbImgLink.getText().substring(0,4).contains("http")) {
            return editBbImgLink.getText();
        } else {
            return imageLink;
        }
    }

    public static void setEditImageLink(String link) {
        imageLink = link;
    }

    /**
     * setter function to set value for GUI elements and variables
     * @param controlPanelGUI
     */
    @Override
    public void setControlPanelComponents(ControlPanelGUI controlPanelGUI) {
        this.controlPanel = controlPanelGUI.controlPanel;
        this.billboardsPane = controlPanelGUI.billboardsPane;
        this.editBbName = controlPanelGUI.editBbName;
        this.editBbMsg = controlPanelGUI.editBbMsg;
        this.editBbInfo = controlPanelGUI.editBbInfo;
        this.editBbImgLink = controlPanelGUI.editBbImgLink;
        this.editUpdateButton = controlPanelGUI.editUpdateButton;
        this.editBbID = controlPanelGUI.editBbID;
        this.viewTable = controlPanelGUI.viewTable;
        this.toEditRow = controlPanelGUI.toEditRow;
        this.EditJPanel = controlPanelGUI.EditJPanel;
        this.editBbColourButton = controlPanelGUI.editBbColourButton;
        this.editMessageColourButton = controlPanelGUI.editMessageColourButton;
        this.editInfoColourButton = controlPanelGUI.editInfoColourButton;
        this.editChooseImageButton = controlPanelGUI.editChooseImageButton;
        this.editPreviewButton = controlPanelGUI.editPreviewButton;
        this.editBbColourPreview = controlPanelGUI.editBbColourPreview;
        this.editMessageColourPreview = controlPanelGUI.editMessageColourPreview;
        this.editInfoColourPreview = controlPanelGUI.editInfoColourPreview;
        this.editCancelButton = controlPanelGUI.editCancelButton;
    }
}
