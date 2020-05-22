import GUIs.ClientGUI;

import javax.swing.*;
import java.io.IOException;

public class ControlPanel {
    public static void main (String[] args){

        SwingUtilities.invokeLater((new Runnable() {
            @Override
            public void run() {
                try {
                    ClientGUI ui = new ClientGUI();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private  static void createGUI() throws IOException, ClassNotFoundException {
        ClientGUI ui = new ClientGUI();
    }
}

