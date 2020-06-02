package Billboard;

import Database.DBConnection;
import User.ServerUserSession;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

/**
 * This class used to create some functions to feedback user's request to databases,
 * and perform corresponding operations in databases
 * @author Jun Chen(n10240977)&Haoze He(n10100351)
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
        String name = (String) data[0];
        String message = (String) data[1];
        String info = (String) data[2];
        String url = (String) data[3];
        String xml = (String) data[4];
        String username = ServerUserSession.getUsername(token);

        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("insert into billboards(billboardID, billboardName, creatorName,message,information, url, xml) values(null,'" + name + "','" + username + "',' " + message + "','" + info + "','" + url + "','" + xml +"');");
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
    public static void delete(Object[] data) throws SQLException{
        int id = (int) data[0];

        System.out.println("ID :" + id);
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("delete from billboards_info where viewID=(" + id+ ");");
        statement.executeQuery("delete from billboards where billboardID=(" + id+ ");");
        statement.close();
    }

    /**
     *This function used to update schedule according to view id in dataBases,
     * when request of user is insert or edit schedule for created billBoard or existed billBoard
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
     *This function used to edit billBoard data in dataBases,
     * when user sends a edit request. set the data and initialize sql queries
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
    public static Object[][] showBillboards() throws SQLException {
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
        return tableData;
    }



}