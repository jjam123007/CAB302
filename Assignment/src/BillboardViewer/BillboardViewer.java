package BillboardViewer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;


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
    private JPanel infoPanel;
    private JPanel imagePanel;
    private JPanel titlePanel;

    // Constructor for the viewer
    public BillboardViewer(Color billboardColour) {
        changeBackground(billboardColour);
        closeBillboardSetup();
    }

    // Show billboard
    public void showBillboard() {
        JFrame frame = new JFrame();

        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(billboardPanel);
        frame.pack();

        // Show the billboard fullscreen
        // Adapted from https://www.tutorialspoint.com/how-to-set-fullscreen-mode-for-java-swing-application
        GraphicsEnvironment graphics = getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(frame);
    }

    // Change the background colour
    public void changeBackground(Color billboardColour) {
        billboardPanel.setBackground(billboardColour);
        titlePanel.setBackground(billboardColour);
        imagePanel.setBackground(billboardColour);
        infoPanel.setBackground(billboardColour);
    }

    // Change the title and its colour of the billboard
    public void changeMessage(String message, Color messageColour) {
        if (message == null) {
            titlePanel.setVisible(false);
        } else {
            titleLabel.setText(message);
            titleLabel.setForeground(messageColour);
            titlePanel.setVisible(true);
        }
    }

    // Change the Image of the billboard
    public void changeImage(String imgInfo) throws Exception {
        if (imgInfo == null) {
            imagePanel.setVisible(false);
        } else {
            // Check if the image is from an URL or Base64 encoded
            if (imgInfo.contains("http")) {
                // URL
                setImageFromURL(imgInfo);
            }
//        else {
//            // Base64
//            setImageFromBase64(imgInfo);
//        }
            imagePanel.setVisible(true);
        }
    }

    // Functions to set image to the billboard
    private void setImageFromURL(String url) throws IOException {
        URL imageURL = new URL(url);
        BufferedImage image = ImageIO.read(imageURL);
        ImageIcon img = resizeImage(new ImageIcon(image));
        imageLabel.setIcon(img);
        imageLabel.setText("");
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    private void setImageFromBase64(String encodedString) throws Exception {
//        File f =  new File("src/BillboardViewer/test_img.jpg");
//        String encodestring = encodeFileToBase64Binary(f);
        byte[] btDataFile = Base64.getDecoder().decode(encodedString);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(btDataFile));
        ImageIcon img = resizeImage(new ImageIcon(image));
        imageLabel.setIcon(img);
        imageLabel.setText("");
    }


    private static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.getEncoder().encodeToString(bytes));
    }
//////////////////////////////////////////////////////////////////////////////////////////////

    // Change the information text and its colour of the billboard
    public void changeInfo(String info, Color infoColour) {
        if (info == null) {
            infoPanel.setVisible(false);
        } else {
            infoLabel.setText((info));
            infoLabel.setForeground(infoColour);
            infoPanel.setVisible(true);
        }
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
    private static ImageIcon resizeImage(ImageIcon image) {
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
            ratio = (screenWidth * 1/2) / imageWidth;
        } else { // Portrait
            ratio = (screenHeight * 1/2) / imageHeight;
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
