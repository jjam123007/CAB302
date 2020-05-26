package BillboardViewer;

import Database.DBConnection;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Iterator;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class Test {
    public static void main(String[] args) throws Exception {
        LocalDate myObj = LocalDate.now(); // Create a date object
        String date = myObj.toString(); // Display the current date
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        System.out.println(time);

        String query = "Select billboardID from schedule where scheduleDate = \"" + date + "\"" +
                " and (startTime <= \"" + time + "\" and endTime >= \"" + time + "\");";

        System.out.println(query);

        try {
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet sqlResult = statement.executeQuery(query);
            sqlResult.afterLast();
            sqlResult.previous();
            String result = sqlResult.getString(1);
            System.out.println(result);
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.getEncoder().encodeToString(bytes));
    }
}
