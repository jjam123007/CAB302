package BillboardViewer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/* Things that needs to be done:
* - Handle when there are no message, img, info,
* - Title: never have a line break (?)
* - Connect to server
* - Query based on database: info, schedule*/

public class BillboardViewer {
    // Components needed for the viewer
    private JPanel billboardPanel;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JLabel imageLabel;
    private JPanel backgroundPanel;

    // Constructor for the viewer
    public BillboardViewer(Color billboardColour) {
        billboardPanel.setBackground(billboardColour);
        backgroundPanel.setBackground(billboardColour);
        closeBillboardSetup();
    }

    // Change the title and its colour of the billboard
    public void changeMessage(String message, Color messageColour) {
        titleLabel.setText(message);
        titleLabel.setForeground(messageColour);
    }

    // Change the Image of the billboard
    public void changeImage(String imgURL) throws IOException {
        ImageIcon img = resizeImage(new ImageIcon(imgURL));

        imageLabel.setIcon(img);
        imageLabel.setText("");
    }

    // Change the information text and its colour of the billboard
    public void changeInfo(String info, Color infoColour) {
        infoLabel.setText((info));
        infoLabel.setForeground(infoColour);
    }

    // Export the billboard
    public JPanel ExportBillboard() {
        return billboardPanel;
    }

    // Function to close the billboard viewer
    private void closeBillboardSetup() {
        // Close on mouse click
        billboardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        // Close on ESC key
        // Adapted from http://www.java2s.com/Code/Java/Swing-JFC/JFramethatcloseswhensomeonepressestheESCkey.htm
        billboardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        billboardPanel.getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
    }

    // Function to resize the image
    private ImageIcon resizeImage(ImageIcon image) {
        // Get the dimensions of the given image
        int imageHeight = image.getIconHeight();
        int imageWidth = image.getIconWidth();

        // Get the dimensions of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenHeight = screenSize.getHeight();
        double screenWidth = screenSize.getWidth();

        // Setup image
        Image setupImage = image.getImage();
        double ratio = 1;

        // Calculate the ratio for resizing
        // Check if the photo is landscape or portrait
        if (imageWidth >= imageHeight) { // Landscape or squared
            // Calculate the ratio
            ratio = (screenWidth * 2/3) / imageWidth;
        } else { // Portrait
            ratio = (screenHeight * 2/3) / imageHeight;
        }

        // Calculate new image size
        int newHeight = (int) Math.round(imageHeight * ratio);
        int newWidth = (int) Math.round(imageWidth * ratio);

        // Resize the image
        Image resizedImage = setupImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon result = new ImageIcon(resizedImage);

        return result;
    }
}
