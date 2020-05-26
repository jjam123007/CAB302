package Billboard;

import Database.DBConnection;
import User.ServerUserSession;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;

public final class ManageBillboards implements Serializable {

    public static void addBillboard(Object[] data, String token) throws SQLException{
        String name = (String) data[0];
        String message = (String) data[1];
        String info = (String) data[2];
        String url = (String) data[3];
        ServerUserSession.addSession("1","1");
        System.out.println(ServerUserSession.getUsername("1"));
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
        statement.executeQuery("insert into billboard values(null,'" + name + "','" + username + "',' " + message + "','" + info + "','" + url + "');");
        statement.executeQuery("insert into billboard_info (BillboardName,creatorName, message, information, url) values('" + name + "','" + username + "',' " + message + "','" + info + "','" + url  + "');");
        //statement.executeQuery("insert into billboard values(null,'" + name + "','" + message + "','" + info + "','" + url + "','" + xml + "');");
        //statement.executeQuery("insert into view (BillboardName, message, info, url) values('" + name + "','" + message + "','" + info + "','" + url + "','" + xml + "');");
        statement.close();
    }

    public static void delete(Object[] data) throws SQLException{
        int id = (int) data[0];

        System.out.println("ID :" + id);
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("delete from view where billboardID=(" + id+ ");");
        statement.close();
    }

    public static void addView (Object[] data) throws SQLException {
        String id = (String) data[0];
        String scheduledDate = (String) data[1];
        String startTime = (String) data[2];
        String endTime = (String) data[3];

        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery("update view set scheduleddate='" + scheduledDate + "', starttime='" + startTime + "', endtime='" + endTime + "'Where billboardId='" + id + "';");
        statement.close();
    }

    public static void edit(Object[] data) throws SQLException {
        System.out.println("Data0: "+data[0]);
        int id = (int) data[0];
        String name = (String) data[1];
        String message = (String) data[2];
        String info = (String) data[3];
        String url = (String) data[4];

        System.out.println("ID to edit :"+id);
        Statement statement = DBConnection.getInstance().createStatement();
        //System.out.println("update billboard set BillboardName="name", message="message"+ data[2]+",info="+ data[3]+",url="+ data[4]+" where billboardID="+ data[0]+";");
        statement.executeQuery("update billboard set BillboardName='"+ name+"', message='"+ message+"',info='"+ info+"',url='"+ url+"' where billboardID='"+ id+"';");
        statement.close();
    }

}