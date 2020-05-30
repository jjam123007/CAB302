import BillboardViewer.BillboardViewer;
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

/**
 * The billboard viewer of the program, works by getting information from the
 * server and display the billboard to the screen
 */
public class DisplayBillboardViewer {
    // Global variables for establishing and maintaining connection to the server
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;
    private static Socket socket;

    // The billboard data
    private static BillboardViewer billboard = null;
    private static String message = null, pictureInfo = null, information = null;
    private static Color billboardColour, messageColour, informationColour;

    /**
     * Create and run an instance of the billboard viewer
     * @param args
     * @throws Exception if the billboard cannot be created from the first place (not happening at all)
     */
    public static void main (String args[]) throws Exception {
        // Create initial billboard
        createBillboard();

        // Setup communication with the server
        setStreams();

        // Constantly receive data from the server
        for (;;) {
            try {
                // Establishing connection to the server
                socket = new Socket("localhost",3310);

                // Set up streams to read and write data
                OutputStream os = socket.getOutputStream();
                InputStream is = socket.getInputStream();
                oos = new ObjectOutputStream(os);
                ois = new ObjectInputStream(is);

                // Get XML
                String input = GetXML.sendXMLRequest(oos, ois);

                // Check if the XML string  is null or not
                if (input == null) {
                    // If null, display an alternative billboard instead
                    setIdleBillboard();

                } else {
                    // Setup billboard
                    readXML(input);

                    // View the billboard
                    billboard.renewBillboard(billboardColour, message, messageColour, information, informationColour, pictureInfo);
                }

                // Close the connection
                oos.close();
                ois.close();
                socket.close();
            } catch (Exception e) {
                // If the server shuts down, display a notification and try to reconnect to the server again
                setStreams();
            }

            // Delay 15 seconds
            Thread.sleep(15000);
        }
    }

    // Function to create a billboard and display the initial loading page
    private static void createBillboard() throws Exception {
        billboard = new BillboardViewer(Color.decode("#ffffff"), "Loading...",
                Color.decode("#000000"), "The billboard is booting up...", Color.decode("#000000"),
                "https://hackernoon.com/drafts/pp6p36ml.png");
    }

    // Function to display the idle billboard
    private static void setIdleBillboard() throws Exception {
        billboard.renewBillboard(Color.decode("#ffffff"), "The billboard is having a break...",
                Color.decode("#000000"), "There is nothing to be displayed right now.", Color.decode("#000000"),
                "https://miro.medium.com/max/10886/1*DFaRILoVj4jv0AAVb6EmDw.jpeg");
    }

    // Function to display the notification billboard saying that it could not connect to the server
    private static void setMaintenanceBillboard() throws Exception {
        billboard.renewBillboard(Color.decode("#f2fc92"), "The server is currently in maintenance.",
                Color.decode("#000000"), null, null,
                "https://media.noria.com/sites/Uploads/2018/12/20/73c77e51-97d0-4ec5-81dc-e80c0b265dcf_Images_ProactiveMaintenanceApproach_31035_1234x694_large.jpeg");
    }

    // Function to read XML string and extract its elements for the billboard
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
            // Text
            Element messageData = (Element) doc.getElementsByTagName("message").item(0);
            message = messageData.getTextContent();

            // Colour
            String messageColourString = messageData.getAttribute("colour");
            messageColour = Color.decode(messageColourString != "" ? messageColourString : "#000000");
        } catch (NullPointerException e) {
            // If there is nothing, return null
            message = null;
        }

        // Get picture URL
        try {
            Element pictureData = (Element) doc.getElementsByTagName("picture").item(0);
            pictureInfo = pictureData.getAttribute("url");
        } catch (NullPointerException e) {
            // If there is nothing, return null
            pictureInfo = null;
        }

        // Get information data
        try {
            // Text
            Element informationData = (Element) doc.getElementsByTagName("information").item(0);
            information = informationData.getTextContent();

            // Colour
            String informationColourString = informationData.getAttribute("colour");
            informationColour = Color.decode(informationColourString != "" ? informationColourString : "#000000");
        } catch (NullPointerException e) {
            // If there is nothing, return null
            information = null;
        }
    }

    // Setup connection to the server
    private static void setStreams() throws Exception {
        // Constantly try to reconnect to the server every 15 seconds
        for (;;) {
            try {
                // Establishing a new connection
                socket = new Socket("localhost",3310);
                System.out.println("Connected to "+ socket.getInetAddress());

                // Set up streams to read and write data
                OutputStream os = socket.getOutputStream();
                InputStream is = socket.getInputStream();
                oos = new ObjectOutputStream(os);
                ois = new ObjectInputStream(is);

                // Send some dummy data to not throw an exception
                oos.writeObject("Dummy data");

                // Close the connection
                oos.close();
                ois.close();
                socket.close();
                break;
            } catch (Exception e) {
                // If could not connect to the server, display a notification billboard
                setMaintenanceBillboard();
            }

            // Delay for 15 seconds
            Thread.sleep(15000);
        }
    }
}
