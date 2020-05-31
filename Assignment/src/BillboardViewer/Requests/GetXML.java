package BillboardViewer.Requests;

import BillboardViewer.BillboardViewer;

import java.io.*;
import java.net.Socket;

/**
 * Contains a function to send a request to the server for retrieving the
 * billboard to be displayed to the viewer
 *
 * @author William Tran (10306234)
 */
public class GetXML {
    /**
     * Send a request from a client to the server to retrieve XML formatted string billboard
     * and store that in the global string currentXML
     *
     * @param oos The client's object output stream
     * @param ois The client's object input stream
     *
     * @return The XML formatted string if found, null otherwise
     */
    public static String sendXMLRequest(ObjectOutputStream oos, ObjectInputStream ois) {
        // Setup the result (the XML formatted string)
        String currentXML = null;

        try {
            // Create a request
            ViewerRequest request = new ViewerRequest(ViewerRequestType.getXML);

            // Send the request to the server
            oos.writeObject(request);
            oos.flush();

            // Listen to server's response
            String result = (String) ois.readObject();

            // Set the result
            currentXML = result;

            // Set the error to false
        } catch (IOException | ClassNotFoundException e) {
            // If the client cannot send and receive data from the server, print a message
            System.out.println("Cannot send and receive data from the server.");
        }

        // Return the result
        return currentXML;
    }
}
