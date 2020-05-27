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


/* Things that needs to be done:
* - Title: never have a line break (?)*/

public class BillboardViewer {

    private JPanel billboardPanel;
    private JLabel titleLabel;
    private JTextPane infoTextPane;
    private JLabel imageLabel;
    private JPanel infoPanel;
    private JPanel imagePanel;
    private JPanel titlePanel;

    // Get the dimensions of the screen
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static final double screenHeight = screenSize.getHeight();
    static final double screenWidth = screenSize.getWidth();

    // The maximum ratio between width and height of every character in font Cambria (rounded)
    // Data from http://jkorpela.fi/x-height.html
    static final double CAMBRIA_WIDTH_RATIO = 0.47;

    // Constructor for the viewer
    public BillboardViewer(Color billboardColour) {
        setupInfoPane();
        infoTextPane.setMargin(new Insets(0, (int) (screenWidth*0.125), 0, (int) (screenWidth*0.125)));
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

    private void setupInfoPane() {
        StyledDocument doc = infoTextPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    public void renewBillboard(Color billboardColour, String message, Color messageColour,
                                String info, Color infoColour, String imgURL) throws Exception {
        // Temporarily turn the billboard down
        billboardPanel.setVisible(false);

        // Default image ratio
        double ratio = 1.0/3;

        // Change the properties of the components
        Boolean noMessage = (message == null);
        Boolean noInfo = (info == null);
        Boolean noImage = (imgURL == null);

        System.out.println(screenHeight + "x" + screenWidth);
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

        // Change the information displayed
        changeBackground(billboardColour);
        changeMessage(message, messageColour);
        changeInfo(info, infoColour);
        changeImage(imgURL, ratio);

        // Turn the billboard up again
        billboardPanel.setVisible(true);
    }

    // Check the content and display in appropriate way

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
            // Set and display the message
            titleLabel.setText(message);
            titleLabel.setForeground(messageColour);
            titlePanel.setVisible(true);
        }
    }

    // Change the Image of the billboard
    public void changeImage(String imgInfo, double ratio) throws Exception {
        if (imgInfo == null) {
            imagePanel.setVisible(false);
        } else {
            // Check if the image is from an URL or Base64 encoded
            if (imgInfo.contains("http")) {
                // URL
                setImageFromURL(imgInfo, ratio);
            }
//        else {
//            // Base64
//            setImageFromBase64(imgInfo);
//        }
            imagePanel.setVisible(true);
        }
    }

    // Functions to set image to the billboard
    private void setImageFromURL(String url, double ratio) throws IOException {
        URL imageURL = new URL(url);
        BufferedImage image = ImageIO.read(imageURL);
        ImageIcon img = resizeImage(new ImageIcon(image), ratio);
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
    public void changeInfo(String info, Color infoColour) {
        if (info == null) {
            infoPanel.setVisible(false);
        } else {
            infoTextPane.setText((info));
            infoTextPane.setForeground(infoColour);
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
