package BillboardViewer;

import Database.DBConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Iterator;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class Test {
    private static BillboardViewer billboard = null;
    private static String message = null, pictureInfo = null, information = null;
    private static Color billboardColour, messageColour, informationColour;

    public static void main(String[] args) throws Exception {
        File a = new File("src/BillboardViewer/text-only.xml");

        // Create a document builder to parse the XML file
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(a);
        doc.getDocumentElement().normalize();

        // Get background color
        String billboardColourString = doc.getDocumentElement().getAttribute("background");
        Color billboardColour = Color.decode(billboardColourString != "" ? billboardColourString : "#ffffff");

        // Get message data
        try {
            Element messageData = (Element) doc.getElementsByTagName("message").item(0);
            message = messageData.getTextContent();
            String messageColourString = messageData.getAttribute("colour");
            messageColour = Color.decode(messageColourString != "" ? messageColourString : "#000000");
        } catch (NullPointerException e) {
            message = null;
        }
        System.out.println(message);

        // Get picture URL
        try {
            Element pictureData = (Element) doc.getElementsByTagName("picture").item(0);
            pictureInfo = pictureData.getAttribute("url");
        } catch (NullPointerException e) {
            pictureInfo = null;
        }
        System.out.println(pictureInfo);

        // Get information data
        try {
            Element informationData = (Element) doc.getElementsByTagName("information").item(0);
            information = informationData.getTextContent();
            String informationColourString = informationData.getAttribute("colour");
            informationColour = Color.decode(informationColourString != "" ? informationColourString : "#000000");
        } catch (NullPointerException e) {
            information = null;
        }
        System.out.println(information);

        createBillboard(billboardColour, message, messageColour, information, informationColour, pictureInfo);
    }

    private static void createBillboard(Color billboardColour, String message, Color messageColour,
                                        String info, Color infoColour, String imgURL) throws Exception {
        // Create an instance of the billboard
        billboard = new BillboardViewer(billboardColour);

        // Change the information displayed
        billboard.changeMessage(message, messageColour);
        billboard.changeInfo(info, infoColour);
        billboard.changeImage(imgURL);

        // Show the billboard
        billboard.showBillboard();
    }

    private static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.getEncoder().encodeToString(bytes));
    }
}
