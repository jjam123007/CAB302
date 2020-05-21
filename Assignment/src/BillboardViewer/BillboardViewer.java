package BillboardViewer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/* Things that needs to be done:
* - Close on click, on ESC
* - Resize img
* - Handle when there are no message, img, info,
* - Title: never have a line break (?)
* - Query based on database: info, schedule*/

public class BillboardViewer {

    private JPanel billboardPanel;
    private JLabel titleLabel;
    private JLabel infoLabel;
    private JLabel imageLabel;

    public void changeMessage(String message) {
        titleLabel.setText(message);
    }

    public void changeImage(String imgURL) throws IOException {
        ImageIcon img = new ImageIcon(imgURL);
        imageLabel.setIcon(img);
        imageLabel.setText("");
    }

    public void changeInfo(String info) {
        infoLabel.setText((info));
    }

    public JPanel ExportBillboard() {
        return billboardPanel;
    }
}
