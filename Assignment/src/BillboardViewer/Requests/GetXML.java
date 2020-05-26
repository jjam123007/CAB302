package BillboardViewer.Requests;

import BillboardViewer.BillboardViewer;

import java.io.*;
import java.net.Socket;

public class GetXML {
    String currentXML = null;
    public Boolean getXMLError = false;

    // Contact the server
    public void sendXMLRequest(ObjectOutputStream oos, ObjectInputStream ois) {
        Socket socket = null;
        try {
            ViewerRequest request = new ViewerRequest(ViewerRequestType.getXML);

            oos.writeObject(request);
            oos.flush();
            String result = (String) ois.readObject();
            System.out.println(result);

            // Set the result
            currentXML = result;

            // Set the error to false
            getXMLError = false;
        } catch (IOException | ClassNotFoundException e) {
            getXMLError = true;
            e.printStackTrace();
        }
    }

    public String returnXML() {
        return currentXML;
    }
}
