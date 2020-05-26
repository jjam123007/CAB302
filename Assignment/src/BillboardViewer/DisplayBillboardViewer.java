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
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;
    private static Socket socket;

    private static BillboardViewer billboard = null;
    private static String message, pictureInfo, information;
    private static Color billboardColour, messageColour, informationColour;

    public static void main (String args[]) throws Exception {

        try {
            setStreams();

            // Get XML
            GetXML newXML = new GetXML();
            newXML.sendXMLRequest(oos, ois);
            String input = newXML.returnXML();

            // Check if the string sent is null or not
            if (input == null) {
                createBillboard(Color.decode("#ffffff"), "The server is currently in maintenance",
                        Color.decode("#000000"), information, Color.decode("#000000"),
                        "https://media.noria.com/sites/Uploads/2018/12/20/73c77e51-97d0-4ec5-81dc-e80c0b265dcf_Images_ProactiveMaintenanceApproach_31035_1234x694_large.jpeg");
            } else {
                // Setup billboard
                readXML(input);

                // View the billboard
                createBillboard(billboardColour, message, messageColour, information, informationColour, pictureInfo);
            }


            for(;;) {
                Thread.sleep(5000);
//                newXML.sendXMLRequest();
                renewBillboard(messageColour,"Sometesting", messageColour, "NewTesting", informationColour, pictureInfo);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readXML(String input) throws ParserConfigurationException, IOException, SAXException {
        // Create a document builder to parse the XML file
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(input)));
        doc.getDocumentElement().normalize();

        // Get background color
        billboardColour = Color.decode(doc.getDocumentElement().getAttribute("background"));

        // Get message data
        Element messageData = (Element) doc.getElementsByTagName("message").item(0);
        message = messageData.getTextContent();
        messageColour = Color.decode(messageData.getAttribute("colour"));
        System.out.println(message);

        // Get picture URL
        Element pictureData = (Element) doc.getElementsByTagName("picture").item(0);
        pictureInfo = pictureData.getAttribute("url");
        System.out.println(pictureInfo);

        // Get information data
        Element informationData = (Element) doc.getElementsByTagName("information").item(0);
        information = informationData.getTextContent();
        informationColour = Color.decode(informationData.getAttribute("colour"));
        System.out.println(information);
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

    private static void renewBillboard(Color billboardColour, String message, Color messageColour,
                                       String info, Color infoColour, String imgURL) throws Exception {
        // Change the information displayed
        billboard.changeBackground(billboardColour);
        billboard.changeMessage(message, messageColour);
        billboard.changeInfo(info, infoColour);
        billboard.changeImage(imgURL);
    }

    private static void setStreams() throws IOException {
        socket = new Socket("localhost",3310);
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        System.out.println("Connected to "+ socket.getInetAddress());
        oos = new ObjectOutputStream(os);
        ois = new ObjectInputStream(is);
    }
}
