package BillboardViewer;

import java.io.*;
import java.net.Socket;

public class GetXML {
    public static void main (String args[]) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 3310);

        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(is);

        ViewerRequest request = new ViewerRequest(ViewerRequestType.getXML);

        oos.writeObject(request);
        oos.flush();
        String aa = (String) ois.readObject();
        System.out.println(aa);
    }
}
