import javax.swing.*;
import java.sql.*;
import java.util.ArrayList; // import the ArrayList class

public class Main {


    public static void main (String [] args) throws SQLException {
        //ArrayList<String[]> data = new ArrayList<String[]>();


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
        App ui = new App();
        JPanel root = ui.getRootPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
