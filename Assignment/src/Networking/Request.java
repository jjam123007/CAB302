package Networking;

import ControlPanel.SerializeArray;

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

    public Request() throws IOException {
        setStreams();
    }

    private void setStreams() throws IOException {
        this.socket = new Socket("localhost",3310);
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        System.out.println("Connected to "+ socket.getInetAddress());
        this.OOS = new ObjectOutputStream(os);
        this.OIS = new ObjectInputStream(is);
    }

    public void closeConnection() throws IOException {
        this.socket.close();
    }

    public void sendRequest(Request request) throws IOException {
        this.OOS.writeObject(request);
        this.OOS.flush();
    }
}
