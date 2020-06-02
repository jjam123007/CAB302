package Networking;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Initiate network connection using data from network.props
 */
public class NetworkConnection {
    public static Socket connectToServer() throws IOException {
        // Read network.props
        Properties props = new Properties();
        FileInputStream in = null;
        in = new FileInputStream("src/Networking/network.props");
        props.load(in);
        in.close();

        // Read the host and port from the file
        String host = props.getProperty("host");
        String port = props.getProperty("port");
        return new Socket(host, Integer.parseInt(port));
    }

    public static ServerSocket openServer() throws IOException {
        // Read network.props
        Properties props = new Properties();
        FileInputStream in = null;
        in = new FileInputStream("src/Networking/network.props");
        props.load(in);
        in.close();

        // Read the port from the file
        String port = props.getProperty("port");
        return new ServerSocket(Integer.parseInt(port));
    }
}
