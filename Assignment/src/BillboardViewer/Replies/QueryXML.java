package BillboardViewer.Replies;

import Database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class QueryXML {
    public static String queryXML() {
        // Retrieve billboard ID
        String billboardID = getBillboardFromSchedule();

        // Generate query to get the xml
        String query = "Select xml from billboard where billboardID = " + billboardID + ";";

        // Initial result
        String result = null;

        // Execute SQL
        try {
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet sqlResult = statement.executeQuery(query);
            sqlResult.next();
            result = sqlResult.getString(1);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return the result
        return result;
    }

    public static String getBillboardFromSchedule() {
        // Get system date and time
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String billboardID = null;

        // Generate query
        String query = "Select billboardID from schedule where scheduleDate = \"" + date + "\"" +
                " and (startTime <= \"" + time + "\" and endTime >= \"" + time + "\");";

        // Execute query
        try {
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet sqlResult = statement.executeQuery(query);

            // Get the latest entry
            sqlResult.afterLast();
            sqlResult.previous();
            billboardID = sqlResult.getString(1);
            System.out.println("Displaying billboard: " + billboardID);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billboardID;
    }
}
