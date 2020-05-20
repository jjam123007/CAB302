import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class ControlPanel {
    public static void main (String[] args){

        SwingUtilities.invokeLater((new Runnable() {
            @Override
            public void run() {
                try {
                    createGUI();

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    private  static void createGUI() throws SQLException, IOException, ClassNotFoundException {
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

