package BillboardViewer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class Test {
    public static void main(String[] args) throws Exception {

//        File f =  new File("src/BillboardViewer/test_img.jpg");
//        String encodestring = encodeFileToBase64Binary(f);
//        System.out.println(encodestring);
//        createBillboard(Color.decode("#0000FF"), "aaa", Color.decode("#0000FF"), "BBB", Color.decode("#0000FF"));

        String message;

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard background=\"#0000FF\">\n" +
                " <message colour=\"#FFFF00\">Welcome to the ____ Corporation's Annual Fundraiser!</message>\n" +
                " <picture url=\"src/BillboardViewer/test_img.jpg\" />\n" +
                " <information colour=\"#00FFFF\">Be sure to check out https://example.com/ for more information.</information>\n" +
                "</billboard>";
    }

    private static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.getEncoder().encodeToString(bytes));
    }


    private static void createBillboard(Color billboardColour, String message, Color messageColour,
                                        String info, Color infoColour) throws Exception {
        // Create an instance of the billboard
        BillboardViewer billboard = new BillboardViewer(billboardColour);

        // Change the information displayed
        billboard.changeMessage(message, messageColour);
        billboard.changeInfo(info, infoColour);
        billboard.imageURL();

        // Show the billboard
        JPanel newBillboard = billboard.ExportBillboard();
        JFrame frame = new JFrame();

        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(newBillboard);
        frame.pack();

        // Show the billboard fullscreen
        // Adapted from https://www.tutorialspoint.com/how-to-set-fullscreen-mode-for-java-swing-application
        GraphicsEnvironment graphics = getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(frame);
    }
}
