package BillboardViewer;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

/**
 * Contains the GUI of the billboard viewer, along with numerous functions
 * to calculating and updating the contents of it
 */

public class BillboardViewer {
    // The components of the viewer
    private JPanel billboardPanel; // The overall panel
    private JLabel titleLabel; // Displays the message
    private JPanel titlePanel; // Determines the area to display the message
    private JTextPane infoTextPane; // Displays the information
    private JPanel infoPanel; // Determines the area to display the information
    private JLabel imageLabel; // Displays the image
    private JPanel imagePanel; // Determines the area to display the image

    // Get the dimensions of the screen
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static final double screenHeight = screenSize.getHeight();
    static final double screenWidth = screenSize.getWidth();

    // The maximum (rounded) ratio between width and height of every character in font Cambria
    // Data from http://jkorpela.fi/x-height.html
    static final double CAMBRIA_WIDTH_RATIO = 0.47;

    /**
     * Constructor of the billboard
     *
     * @param billboardColour Specifies the background colour of the billboard
     * @param message Specifies the message text of the billboard
     * @param messageColour Specifies the message colour of the billboard
     * @param info Specifies the information text of the billboard
     * @param infoColour Specifies the information colour of the billboard
     * @param imgURL Specifies the image URL or Base64 encoded data of the billboard
     * @throws Exception if the image cannot be read
     */
    public BillboardViewer(Color billboardColour, String message, Color messageColour,
                           String info, Color infoColour, String imgURL) throws Exception {
        // Setup the styles of the information text pane
        setupInfoPane();

        // Change the data of the components
        renewBillboard(billboardColour, message, messageColour, info, infoColour, imgURL);

        // Setup event listeners to close the billboard viewer
        closeBillboardSetup();

        // Show the billboard
        showBillboard();
    }

    // Set up and display the billboard in fullscreen
    private void showBillboard() {
        // Create a new frame to contain all the components
        JFrame frame = new JFrame();

        // Some initial setups
        frame.setUndecorated(true); // Not showing the window bar
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the frame on close
        frame.setContentPane(billboardPanel); // Set the contents
        frame.pack(); // Pack the contents

        // Show the billboard fullscreen
        // Adapted from https://www.tutorialspoint.com/how-to-set-fullscreen-mode-for-java-swing-application
        GraphicsEnvironment graphics = getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(frame);
    }

    // Set up the styles of the information pane
    private void setupInfoPane() {
        // Align the text center
        StyledDocument doc = infoTextPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Set the margins so that the text fills up 75% of the screen width
        infoTextPane.setMargin(new Insets(0, (int) (screenWidth*0.125), 0, (int) (screenWidth*0.125)));
    }

    /**
     * Renew the information displayed in the billboard if the data is changed
     *
     * @param billboardColour Background colour
     * @param message Message text
     * @param messageColour Message colour
     * @param info Information text
     * @param infoColour Information colour
     * @param imgURL The URL or Base64 encoded image
     * @throws Exception if the function fails to read the image
     */
    public void renewBillboard(Color billboardColour, String message, Color messageColour,
                                String info, Color infoColour, String imgURL) throws Exception {
        // Check if the billboard needs to be renew or not
        if (billboardHasChanged(billboardColour, message, messageColour, info, infoColour)) {
            // Default image ratio
            double ratio = 1.0/3;

            // Change the properties of the components
            Boolean noMessage = (message == null);
            Boolean noInfo = (info == null);
            Boolean noImage = (imgURL == null);

            // Calculate message size in every situation
            int messageFontSize = (int) (message != null? Math.round((screenWidth/message.length())/CAMBRIA_WIDTH_RATIO) : 0);
            if (messageFontSize > screenHeight / 9) {
                messageFontSize = (int) Math.round(screenHeight / 9);
            }

            // Check the existence of data
            if (noMessage) {
                if (noImage) {
                    // Only information
                    // Calculate the font size
                    int fontSize = (int) Math.round((screenWidth/info.length())/CAMBRIA_WIDTH_RATIO * 3);
                    System.out.println(fontSize);
                    infoTextPane.setFont(new Font("Cambria", Font.PLAIN, fontSize));
                } else if (noInfo) {
                    // Only image
                    ratio = 1.0/2;
                } else {
                    // Information and image
                    ratio = 1.0/2;

                    // Set the image panel size
                    imagePanel.setPreferredSize(new Dimension((int) screenWidth, (int) screenHeight/3));
                }
            } else if (noImage) {
                // Set message font size
                titleLabel.setFont(new Font("Cambria", Font.PLAIN, messageFontSize));
                if (!noInfo) {
                    // Message and information
                    // Calculate the font size

                    // Set back the panel size
                    infoPanel.setPreferredSize(new Dimension((int) screenWidth, (int) screenHeight/10));
                }
                // Else -> message only: do nothing
            } else if (noInfo) {
                // Message and image
                // Set message font size
                titleLabel.setFont(new Font("Cambria", Font.PLAIN, messageFontSize));

                // Set image ratio
                ratio = 1.0/2;

                // Set the image panel size
                imagePanel.setPreferredSize(new Dimension((int) screenWidth, (int) screenHeight/3));

            } else {
                // Has all 3 of them
                // Set message font size
                titleLabel.setFont(new Font("Cambria", Font.PLAIN, messageFontSize));
            }

            // Temporarily turn the billboard down to make changes to the components
            billboardPanel.setVisible(false);

            // Change the information displayed
            changeBackground(billboardColour);
            changeMessage(message, messageColour);
            changeInfo(info, infoColour);
            changeImage(imgURL, ratio);

            // Turn the billboard up again
            billboardPanel.setVisible(true);
        }
    }

    // Change the background colour
    private void changeBackground(Color billboardColour) {
        // Change for each component
        billboardPanel.setBackground(billboardColour);
        titlePanel.setBackground(billboardColour);
        imagePanel.setBackground(billboardColour);
        infoPanel.setBackground(billboardColour);
        infoTextPane.setBackground(billboardColour);
    }

    // Change the title and its colour of the billboard
    private void changeMessage(String message, Color messageColour) {
        if (message == null) {
            // If there is no message
            // Set the message panel invincible
            titlePanel.setVisible(false);
        } else {
            // Set the message and its colour
            titleLabel.setText(message);
            titleLabel.setForeground(messageColour);

            // Set the message visible
            titlePanel.setVisible(true);
        }
    }

    // Change the image of the billboard
    private void changeImage(String imgInfo, double ratio){
        if (imgInfo == null) {
            // If there is no image
            // Set the image panel invincible
            imagePanel.setVisible(false);
        } else {
            // Check if the image is from an URL or Base64 encoded
            try {
                if (imgInfo.contains("http")) {
                    // URL
                    setImageFromURL(imgInfo, ratio);
                }
//                else {
//                    // Base64
//                    setImageFromBase64(imgInfo);
//                }
                imagePanel.setVisible(true);
            } catch (IOException e) {
                // If the image cannot be read, print out a message to the console and set image invincible
                System.out.println("There's something wrong with the image");
                imagePanel.setVisible(false);
            }
        }
    }

    // Functions to set image from URL to the billboard
    private void setImageFromURL(String url, double ratio) throws IOException {
        // Read image from URL
        URL imageURL = new URL(url);
        BufferedImage image = ImageIO.read(imageURL);

        // Resize the image
        ImageIcon img = resizeImage(new ImageIcon(image), ratio);

        // Set and display the image
        imageLabel.setIcon(img);
        imageLabel.setText("");
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    private void setImageFromBase64(String encodedString) throws Exception {
//        File f =  new File("src/BillboardViewer/test_img.jpg");
//        String encodestring = encodeFileToBase64Binary(f);
        byte[] btDataFile = Base64.getDecoder().decode(encodedString);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(btDataFile));
//        ImageIcon img = resizeImage(new ImageIcon(image));
//        imageLabel.setIcon(img);
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
    private void changeInfo(String info, Color infoColour) {
        if (info == null) {
            // If there is no information
            // Set the information panel invincible
            infoPanel.setVisible(false);
        } else {
            // Set the information and its colour
            infoTextPane.setText((info));
            infoTextPane.setForeground(infoColour);

            // Set the information visible
            infoPanel.setVisible(true);
        }
    }

    // Check if the information in the billboard is new or not
    private Boolean billboardHasChanged(Color billboardColour, String message, Color messageColour,
                                               String info, Color infoColour) {
        // Check if each component is still the same
        if (billboardColour.equals(billboardPanel.getBackground()) && (message.equals(titleLabel.getText()))
                && messageColour.equals(titleLabel.getForeground()) && (info.equals(infoTextPane.getText()))
                && infoColour.equals(infoTextPane.getForeground())) {
            return false; // not changed
        } else {
            return true; // has changed
        }
    }

    // Function to close the billboard viewer
    private void closeBillboardSetup() {
        // Close on mouse click for each components
        billboardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        infoTextPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        infoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        titlePanel.addMouseListener(new MouseAdapter() {
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
    private static ImageIcon resizeImage(ImageIcon image, double maxRatio) {
        // Get the dimensions of the given image
        int imageHeight = image.getIconHeight();
        int imageWidth = image.getIconWidth();

        // Setup image
        Image setupImage = image.getImage();
        double ratio = 1;

        // Calculate the ratio for resizing
        // Check if the photo is landscape or portrait
        if (imageWidth > imageHeight) { // Landscape
            // Calculate the ratio
            ratio = (screenWidth * maxRatio) / imageWidth;
        } else { // Portrait or squared
            ratio = (screenHeight * maxRatio) / imageHeight;
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
