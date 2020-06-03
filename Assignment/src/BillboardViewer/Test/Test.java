package BillboardViewer.Test;

import BillboardViewer.BillboardViewer;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Iterator;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

/* A test class when developing the billboard viewer
Really similar to DisplayBillboardViewer.java
Using the xml files in the same folder for testing purposes */
public class Test {
    private static BillboardViewer billboard = null;
    private static String message = null, pictureInfo = null, information = null;
    private static Color billboardColour, messageColour, informationColour;

    // The path for testing
    private static String filePath = "src/BillboardViewer/Test/img-only.xml";

    // The main function
    public static void main(String[] args) throws Exception {

        File a = new File(filePath);

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
            if (pictureData.hasAttribute("url")){
                pictureInfo = pictureData.getAttribute("url");
            } else {
                pictureInfo = pictureData.getAttribute("data");
            }
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

        billboard = new BillboardViewer(Color.decode("#ffffff"), "Loading...",
                Color.decode("#000000"), "Loading...", Color.decode("#000000"),
                "https://i.ytimg.com/vi/Gu24piLwtOg/maxresdefault.jpg", true);
        billboard.renewBillboard(billboardColour, message, messageColour, information, informationColour, pictureInfo);
//        Thread.sleep(5000);
//        billboard.renewBillboard(Color.decode("#ffffff"), "Loading...",
//                Color.decode("#000000"), "Loading...", Color.decode("#000000"),
//                "https://i.ytimg.com/vi/Gu24piLwtOg/maxresdefault.jpg");
//        Thread.sleep(3000);
//        billboard.renewBillboard(billboardColour, message, messageColour, information, informationColour, pictureInfo);
    }

    private static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.getEncoder().encodeToString(bytes));
    }
}
