package BillboardViewer;

import java.io.*;
import java.net.Socket;

public class GetXML {
    String currentXML = null;
    public Boolean getXMLError = false;

//    public static void main (String args[]) throws IOException, ClassNotFoundException {
//
//    }

    // Contact the server
    public void sendXMLRequest() {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 3310);

            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            ObjectOutputStream oos = new ObjectOutputStream(os);
            ObjectInputStream ois = new ObjectInputStream(is);

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
