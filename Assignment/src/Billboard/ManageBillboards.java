package Billboard;

import Database.DBConnection;
import User.ServerUserSession;

import javax.xml.transform.Result;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * This class used to create some functions to feedback user's request to databases,
 * and perform corresponding operations in databases
 * @author Jun Chen(n10240977) & Haoze He(n10100351) & William Tran (n10306234)
 */
public final class ManageBillboards implements Serializable {
    /**
     * This function used to add new billBoard data into dataBases,
     * when the request is create new Billboard, it calls the addBillboard method.
     * @param data
     * @param token
     * @throws SQLException
     */
    public static void addBillboard(Object[] data, String token) throws SQLException{
        // Get the billboard information
        String name = (String) data[0];
        String message = (String) data[1];
        String info = (String) data[2];
        String url = (String) data[3];
        String xml = (String) data[4];
        String username = ServerUserSession.getUsername(token);

        // Add to the database
        // Billboard table
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("insert into billboards(billboardID, billboardName, creatorName,message,information, url, xml) values(null,'" + name + "','" + username + "',' " + message + "','" + info + "','" + url + "','" + xml +"');");

        // Billboard Info Table
        ResultSet sqlResult = statement.executeQuery("select billboardID from billboards;");
        sqlResult.afterLast();
        sqlResult.previous();
        String billboardID = sqlResult.getString(1);
        statement.executeQuery("insert into billboards_info (viewID, billboardName,creatorName, message, information, url) values(" + billboardID + ",'" + name + "','" + username + "','" + message + "','" + info + "','" + url  + "');");
        statement.close();
    }

    /**
     *This function used to delete a billBoard from dataBases,
     * when user sends a delete request. set the data and initialize sql queries
     * @param data
     * @throws SQLException
     */
    public static void delete(Object[] data) throws SQLException {
        int id = (int) data[0];

        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("delete from billboards_info where viewID=(" + id + ");");
        statement.executeQuery("delete from billboards where billboardID=(" + id + ");");
        statement.close();
    }

    /**
     *This function used to update latest schedule according to view id in dataBases,
     * when request of user is insert or edit schedule for created billBoard or existed billBoard
     *
     * @param data
     * @throws SQLException
     */
    public static void addSchedule(Object[] data) throws SQLException {
        String id = (String) data[0];
        String scheduledDate = (String) data[1];
        String startTime = (String) data[2];
        String endTime = (String) data[3];

        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("INSERT INTO schedules values (null, " + id + ",'" + scheduledDate + "','" + startTime + "','" + endTime + "');");
        statement.executeQuery("update billboards_info set scheduleddate='" + scheduledDate + "', starttime='" + startTime + "', endtime='" + endTime + "'Where viewID='" + id + "';");
        statement.close();
    }

    /**
     *This function used to edit billBoard data in dataBases,
     * when user sends a edit request. set the data and initialize sql queries
     * @param data
     * @throws SQLException
     */
    public static void edit(Object[] data) throws SQLException {
        int id = (int) data[0];
        String name = (String) data[1];
        String message = (String) data[2];
        String info = (String) data[3];
        String url = (String) data[4];
        String xml = (String) data[5];
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("update billboards set billboardName='"+ name+"', message='"+ message+"',information='"+ info+"',url='"+ url+ "',xml='" + xml + "' where billboardID='"+ id+"';");
        statement.executeQuery("update billboards_info set billboardName='"+ name+"', message='"+ message+"',information='"+ info+"',url='"+ url+"' where viewID='"+ id+"';");
        statement.close();
    }

    /**
     * This method used to get all billBoard data from databases,
     * then show in view interface.
     * @return tableDataArray
     * @throws SQLException
     */
    public static Object[][] showBillboards() throws SQLException, ParseException {
        Object[][] tableData;

        Statement statement = DBConnection.getInstance().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM billboards_info");

        int rowcount = 0;

        if (resultSet.last()) {
            rowcount = resultSet.getRow();
            resultSet.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
        }
        tableData = new Object[rowcount][9];

        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < rowcount; i++) {
            resultSet.next();
            String viewID = Integer.toString(resultSet.getInt(1));
            String BillboardName = resultSet.getString(2);
            String creatorName = resultSet.getString(3);
            String msg = resultSet.getString(4);
            String info = resultSet.getString(5);
            String url = resultSet.getString(6);
            String scheduledDate = null;
            if (!(resultSet.getString(7) == null)){
                scheduledDate = newDateFormat.format(oldDateFormat.parse(resultSet.getString(7)));
            }
            Time startTime = resultSet.getTime(8);
            Time endTime = resultSet.getTime(9);
            Object[] myString = {viewID, BillboardName,creatorName, msg,info, url, scheduledDate, startTime, endTime};
            tableData[i] = myString;
        }
        statement.close();
        return tableData;
    }

    /**
     * This method used to get schedule data for a specific billboard name from databases,
     * then show in schedule pane to view the calender structure of billboards.
     * @return tableDataArray
     * @throws SQLException
     */
    public static Object[][] showSchedule(Object[] data) throws SQLException, ParseException {
        String billboardID = (String) data[0];

        // Get system date and time
        String date = LocalDate.now().toString();

        // Initiate result
        Object[][] tableData;

        // Query from the database
        Statement statement = DBConnection.getInstance().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT scheduleID, billboardID, scheduledDate, startTime, endTime FROM schedules where billboardID=" + billboardID +
                " AND scheduledDate>='" + date + "';");
        ResultSet billboardName = statement.executeQuery("SELECT billboardName FROM billboards where billboardID=" + billboardID + ";");
        billboardName.next();

        // Some preparation for date formatting
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Get the number of results
        int rowcount = 0;
        if (resultSet.last()) {
            rowcount = resultSet.getRow();
            resultSet.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
        }

        tableData = new Object[rowcount][5];

        // Populate the result to the array
        for (int i = 0; i < rowcount; i++) {
            resultSet.next();
            String BillboardName = billboardName.getString(1);
            String scheduleID = resultSet.getString(1);
            String scheduledDate = newDateFormat.format(oldDateFormat.parse(resultSet.getString(3)));
            Time startTime = resultSet.getTime(4);
            Time endTime = resultSet.getTime(5);
            Object[] myString = {scheduleID, billboardID, BillboardName,scheduledDate, startTime, endTime};
            tableData[i] = myString;
        }

        // Close the connection
        statement.close();

        // Return the result
        return tableData;
    }

    /**
     * This method used to get all schedule data,
     * then show in schedule pane to view the calender structure of billboards.
     * @return tableDataArray
     * @throws SQLException
     */
    public static Object[][] showAllSchedule() throws SQLException, ParseException {
        Object[][] tableData;

        // Get system date and time
        String date = LocalDate.now().toString();

        // Get date after 7 days
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        String newDate = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());

        Statement statement = DBConnection.getInstance().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT scheduleID, billboardID, scheduledDate, startTime, endTime FROM schedules WHERE scheduledDate>='" + date + "' AND scheduledDate <= '"+ newDate +"';");

        // Some preparation for date formatting
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Get the number of results
        int rowcount = 0;
        if (resultSet.last()) {
            rowcount = resultSet.getRow();
            resultSet.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
        }

        tableData = new Object[rowcount][5];

        // Populate the array with the result
        for (int i = 0; i < rowcount; i++) {
            resultSet.next();
            String scheduleID = resultSet.getString(1);
            String billboardID = resultSet.getString(2);
            ResultSet billboardName = statement.executeQuery("SELECT billboardName FROM billboards where billboardID=" + billboardID + ";");

            String BillboardName;
            if (billboardName.next()){
                BillboardName = billboardName.getString(1);
            } else {
                BillboardName = "(Deleted)";
            }
            String scheduledDate = newDateFormat.format(oldDateFormat.parse(resultSet.getString(3)));
            Time startTime = resultSet.getTime(4);
            Time endTime = resultSet.getTime(5);
            Object[] myString = {scheduleID, billboardID, BillboardName,scheduledDate, startTime, endTime};
            tableData[i] = myString;
        }

        // Close the connection and return the result
        statement.close();
        return tableData;
    }

    /**
     * Delete a selected schedule from the database
     */
    public static void deleteSchedule(Object[] data) throws SQLException{
        String scheduleID = (String) data[0];

        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("delete from schedules where scheduleID=(" + scheduleID + ");");
        statement.close();
    }
}