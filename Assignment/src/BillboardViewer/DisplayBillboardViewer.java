package BillboardViewer;

import BillboardViewer.Requests.GetXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class DisplayBillboardViewer {

    private static BillboardViewer billboard = null;
    private static String message = null, pictureInfo = null, information = null;
    private static Color billboardColour, messageColour, informationColour;

    public static void main (String args[]) throws Exception {
        // Create initial billboard
        setupBillboard(Color.decode("#ffffff"), "Loading...",
                Color.decode("#000000"), "The billboard is booting up...", Color.decode("#000000"),
                "https://hackernoon.com/drafts/pp6p36ml.png");

        // Constantly receive data from the server
        for (;;) {
            try {

                // Get XML
                GetXML newXML = new GetXML();
                newXML.sendXMLRequest();
                String input = newXML.returnXML();

                // Check if the string sent is null or not
                if (input == null) {
                    billboard.renewBillboard(Color.decode("#ffffff"), "The server is currently in maintenance",
                            Color.decode("#000000"), "Something something", Color.decode("#000000"),
                            "https://media.noria.com/sites/Uploads/2018/12/20/73c77e51-97d0-4ec5-81dc-e80c0b265dcf_Images_ProactiveMaintenanceApproach_31035_1234x694_large.jpeg");
                } else {
                    // Setup billboard
                    readXML(input);

                    // View the billboard
                    billboard.renewBillboard(billboardColour, message, messageColour, information, informationColour, pictureInfo);
                }

                // Delay
                Thread.sleep(3000);

            } catch (Exception e) {
                billboard.renewBillboard(Color.decode("#f2fc92"), "The server is currently in maintenance",
                        Color.decode("#000000"), information, Color.decode("#000000"),
                        "https://media.noria.com/sites/Uploads/2018/12/20/73c77e51-97d0-4ec5-81dc-e80c0b265dcf_Images_ProactiveMaintenanceApproach_31035_1234x694_large.jpeg");
            }
        }
    }

    private static void readXML(String input) throws ParserConfigurationException, IOException, SAXException {
        // Create a document builder to parse the XML file
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(input)));
        doc.getDocumentElement().normalize();

        // Get background color
        String billboardColourString = doc.getDocumentElement().getAttribute("background");
        billboardColour = Color.decode(billboardColourString != "" ? billboardColourString : "#ffffff");

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
    }

    private static void setupBillboard(Color billboardColour, String message, Color messageColour,
                                        String info, Color infoColour, String imgURL) throws Exception {
        // Create an instance of the billboard
        billboard = new BillboardViewer(billboardColour);

        // Change the information displayed
        billboard.changeMessage(message, messageColour);
        billboard.changeInfo(info, infoColour);
        billboard.changeImage(imgURL, 1.0/3);

        // Show the billboard
        billboard.showBillboard();
    }

}
