package BillboardViewer.Replies;

import Database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Contains two static functions to query the billboard that needs to be display at
 * local machine's time and return its XML formatted string stored to the database
 *
 * @author William Tran - 10306234
 */

public final class QueryXML {
    /**
     * Get XML formatted string from the database based on local machine's time
     *
     * @return billboard XML formatted string if found, null otherwise
     */
    public static String queryXML() {
        // Retrieve billboard ID
        String billboardID = getBillboardFromSchedule();

        // Check if there is any billboard required to be displayed
        if (billboardID != null) {
            // Generate query to get the xml
            String query = "Select xml from billboard where billboardID = " + billboardID + ";";

            // Setup initial result
            String result = null;

            try {
                // Establish a connection to the database
                Statement statement = DBConnection.getInstance().createStatement();

                // Execute the SQL query
                ResultSet sqlResult = statement.executeQuery(query);

                // Retrieve the result
                sqlResult.next();
                result = sqlResult.getString(1);

                // Close the connection
                statement.close();
            } catch (SQLException e) {
                // If the billboard is not found, print a message to the server
                System.out.println("Billboard not found. Please check the billboard from the control panel.");
            }

            // Return the result
            return result;
        } else {
            // If there is no billboard to be displayed, return null
            return null;
        }
    }

    /**
     * Retrieve local machine date and time and determine which billboard should be
     * displayed from the schedule stored in the database
     *
     * @return the billboard ID that should be displayed, null if there aren't any
     */
    public static String getBillboardFromSchedule() {
        // Get system date and time
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // Setup initial result
        String billboardID = null;

        // Generate query
        String query = "Select billboardID from schedule where scheduleDate = \"" + date + "\"" +
                " and (startTime <= \"" + time + "\" and endTime >= \"" + time + "\");";

        try {
            // Establish a connection to the database
            Statement statement = DBConnection.getInstance().createStatement();

            // Execute the SQL query
            ResultSet sqlResult = statement.executeQuery(query);

            // Get the latest entry and retrieve the result
            sqlResult.afterLast();
            sqlResult.previous();
            billboardID = sqlResult.getString(1);

            // Close the connection
            statement.close();
        } catch (SQLException e) {
            // If there is no billboard to be displayed, print a message
            System.out.println("There is no billboard to be displayed right now.");
        }

        // Return the result
        return billboardID;
    }
}
