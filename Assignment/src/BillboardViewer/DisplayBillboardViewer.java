package BillboardViewer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class DisplayBillboardViewer {
    private static void createBillboard(Color billboardColour, String message, Color messageColour,
                                        String info, Color infoColour, String imgURL) throws Exception {
        // Create an instance of the billboard
        BillboardViewer billboard = new BillboardViewer(billboardColour);

        // Change the information displayed
        billboard.changeMessage(message, messageColour);
        billboard.changeInfo(info, infoColour);
        billboard.changeImage(imgURL);

        // Show the billboard
        billboard.showBillboard();
    }

    public static void main (String args[]) throws Exception {
        try {
            // Adapted from https://www.javatpoint.com/how-to-read-xml-file-in-java
            // Get input from the XML file
//            File inputFile = new File("src/BillboardViewer/test.xml");

            GetXML newXML = new GetXML();
            newXML.sendXMLRequest();
            String input = newXML.currentXML;
            // Create a document builder to parse the XML file
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(input)));
            doc.getDocumentElement().normalize();

            // Get background color
            Color billboardColour = Color.decode(doc.getDocumentElement().getAttribute("background"));

            // Get message data
            Element messageData = (Element) doc.getElementsByTagName("message").item(0);
            String message = messageData.getTextContent();
            Color messageColour = Color.decode(messageData.getAttribute("colour"));
            System.out.println(message);

            // Get picture URL
            Element pictureData = (Element) doc.getElementsByTagName("picture").item(0);
            String pictureURL = pictureData.getAttribute("url");
            System.out.println(pictureURL);

            // Get information data
            Element informationData = (Element) doc.getElementsByTagName("information").item(0);
            String information = informationData.getTextContent();
            Color informationColour = Color.decode(informationData.getAttribute("colour"));
            System.out.println(information);

            // View the billboard
            createBillboard(billboardColour, message, messageColour, information, informationColour, pictureURL);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
