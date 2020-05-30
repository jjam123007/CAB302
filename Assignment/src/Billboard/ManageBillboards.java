package Billboard;

import ControlPanel.SerializeArray;
import Database.DBConnection;
import User.ServerUserSession;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

/**
 * @author Jun Chen(n10240977)&Haoze He(n10100351)
 */
public final class ManageBillboards implements Serializable {
    /**
     *
     * @param data
     * @param token
     * @throws SQLException
     */
    public static void addBillboard(Object[] data, String token) throws SQLException{
        String name = (String) data[0];
        String message = (String) data[1];
        String info = (String) data[2];
        String url = (String) data[3];
        String username = ServerUserSession.getUsername(token);

        System.out.println("Token: "+token);
        System.out.println("CreatorName: "+ username);
        System.out.println("Name :" + name);
        System.out.println("Msg :" + message);
        System.out.println("Info :" + info);
        System.out.println("Url :" + url);

        // Create and store XML format to the database
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard background=\"#0000FF\">\n" +
                " <message colour=\"#FFFF00\">" + message + "</message>\n" +
                " <picture url=\"" + url + "\" />\n" +
                " <information colour=\"#00FFFF\">" + info + "</information>\n" +
                "</billboard>";

        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("insert into billboards(billboardID, billboardName, creatorName,message,information, url) values(null,'" + name + "','" + username + "',' " + message + "','" + info + "','" + url + "');");
        statement.executeQuery("insert into billboards_info (billboardName,creatorName, message, information, url) values('" + name + "','" + username + "',' " + message + "','" + info + "','" + url  + "');");
        //statement.executeQuery("insert into billboard values(null,'" + name + "','" + message + "','" + info + "','" + url + "','" + xml + "');");
        //statement.executeQuery("insert into view (BillboardName, message, info, url) values('" + name + "','" + message + "','" + info + "','" + url + "','" + xml + "');");
        statement.close();
    }

    /**
     *
     * @param data
     * @throws SQLException
     */
    public static void delete(Object[] data) throws SQLException{
        int id = (int) data[0];

        System.out.println("ID :" + id);
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("delete from billboards_info where viewID=(" + id+ ");");
        statement.executeQuery("delete from billboards where billboardID=(" + id+ ");");
        statement.close();
    }

    /**
     *
     * @param data
     * @throws SQLException
     */
    public static void addView (Object[] data) throws SQLException {
        String id = (String) data[0];
        String scheduledDate = (String) data[1];
        String startTime = (String) data[2];
        String endTime = (String) data[3];

        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("update billboards_info set scheduleddate='" + scheduledDate + "', starttime='" + startTime + "', endtime='" + endTime + "'Where viewID='" + id + "';");
        statement.close();
    }

    /**
     *
     * @param data
     * @throws SQLException
     */
    public static void edit(Object[] data) throws SQLException {
        System.out.println("Data0: "+data[0]);
        int id = (int) data[0];
        String name = (String) data[1];
        String message = (String) data[2];
        String info = (String) data[3];
        String url = (String) data[4];
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("update billboards set billboardName='"+ name+"', message='"+ message+"',information='"+ info+"',url='"+ url+"' where billboardID='"+ id+"';");
        statement.executeQuery("update billboards_info set billboardName='"+ name+"', message='"+ message+"',information='"+ info+"',url='"+ url+"' where viewID='"+ id+"';");
        statement.close();
    }
    public static SerializeArray showBillboards() throws SQLException {
        Object[][] tableData;

        Statement statement = DBConnection.getInstance().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM billboards_info");

        int rowcount = 0;

        if (resultSet.last()) {
            rowcount = resultSet.getRow();
            resultSet.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
        }
        tableData = new Object[rowcount][9];

        for (int i = 0; i < rowcount; i++) {
            resultSet.next();
            String viewID = Integer.toString(resultSet.getInt(1));
            String BillboardName = resultSet.getString(2);
            String creatorName = resultSet.getString(3);
            String msg = resultSet.getString(4);
            String info = resultSet.getString(5);
            String url = resultSet.getString(6);
            String scheduledDate = resultSet.getString(7);
            Time startTime = resultSet.getTime(8);
            Time endTime = resultSet.getTime(9);
            Object[] myString = {viewID, BillboardName,creatorName, msg,info, url, scheduledDate, startTime, endTime};
            tableData[i] = myString;
        }
        statement.close();
        SerializeArray tableDataArray = new SerializeArray(tableData);
        return tableDataArray;
    }



}