import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main (String[] args){

        SwingUtilities.invokeLater((new Runnable() {
            @Override
            public void run() {
                try {
                    createGUI();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    private  static void createGUI() throws SQLException {
        clientGUI ui = new clientGUI();
        JPanel root = ui.getRootPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

