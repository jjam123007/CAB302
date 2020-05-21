package BillboardViewer;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class BillboardViewer {
    public static void main (String args[]) {
        try {
            // Adapted from https://www.javatpoint.com/how-to-read-xml-file-in-java
            // Get input from the XML file
            File inputFile = new File("src/BillboardViewer/test.xml");

            // Create a document builder to parse the XML file
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            // Get message data
            Element messageData = (Element) doc.getElementsByTagName("message").item(0);
            String message = messageData.getTextContent();
            String messageColour = messageData.getAttribute("colour");
            System.out.println(message);
            System.out.println(messageColour);

            // Get picture URL
            Element pictureData = (Element) doc.getElementsByTagName("picture").item(0);
            String pictureURL = pictureData.getAttribute("url");
            System.out.println(pictureURL);

            // Get information data
            Element informationData = (Element) doc.getElementsByTagName("information").item(0);
            String information = informationData.getTextContent();
            String informationColour = pictureData.getAttribute("colour");
            System.out.println(information);
            System.out.println(informationColour);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
