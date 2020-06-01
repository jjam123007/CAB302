package Networking;

import java.io.*;
import java.net.Socket;

public class
Request implements Serializable{
    private static Socket socket;
    protected static ObjectOutputStream OOS;
    protected static ObjectInputStream OIS;

    public ObjectInputStream getOIS() {
        return OIS;
    }

    /**
     * Create a server request.
     * @throws IOException
     */
    public Request() throws IOException {
        setStreams();
    }

    //Set the object output and input streams between the client and the server.
    private void setStreams() throws IOException {
        this.socket = new Socket("localhost",3310);
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        System.out.println("Connected to "+ socket.getInetAddress());
        this.OOS = new ObjectOutputStream(os);
        this.OIS = new ObjectInputStream(is);
    }

    /**
     * Close the current client-to-server connection.
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        this.socket.close();
    }

    /**
     * Send the request to the server.
     * @throws IOException
     */
    public void sendRequest(Request request) throws IOException {
        this.OOS.writeObject(request);
        this.OOS.flush();
    }
}
