import GUIs.ClientGUI;
import GUIs.LoginGUI;

import javax.swing.*;
import java.io.IOException;

public class ControlPanel {
    public static void main (String[] args){

        SwingUtilities.invokeLater((new Runnable() {
            @Override
            public void run() {
                try {
                    LoginGUI login = new LoginGUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private  static void createGUI() throws IOException, ClassNotFoundException {
        ClientGUI ui = new ClientGUI();
        JPanel root = ui.getRootPanel();
        JFrame frame = new JFrame();
        frame.setContentPane(root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

