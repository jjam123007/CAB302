package Billboard;

import Database.DBConnection;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author Jun Chen(n10240977)&Haoze He(n10100351)
 */
public class BillboardDataSource {

    //set up the database
    public static final String create_billboardDB = "create database if not exists `billboardscheduler`;";

    /**
     *
     * @throws SQLException
     */
    public static void create_billboardTable() throws SQLException {
        //create billboards table
        String create_billboardTable = "CREATE TABLE IF NOT EXISTS billboards " +
                "(billboardID int NOT NULL auto_increment primary key, " +
                "billboardName varchar(60)," +
                "creatorName varchar(60)," +
                "message varchar(2000)," +
                "information varchar(2000)," +
                "url text," +
                "xml text" +
                ");";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_billboardTable);
        statement.close();
    }

    /**
     *
     * @throws SQLException
     */
    public static void create_viewTable() throws SQLException {
        //create billboards_info table for (view)
        String create_viewTable =
                "CREATE TABLE IF NOT EXISTS billboards_info (" +
                        "    viewID int NOT NULL auto_increment primary key," +
                        "    billboardName varchar(60)," +
                        "    creatorName varchar(60)," +
                        "    message varchar(2000)," +
                        "    information varchar(2000)," +
                        "    url varchar(2000)," +
                        "    scheduledDate Date," +
                        "    startTime Time," +
                        "    endTime Time" +
                        "	);";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_viewTable);
        statement.close();
    }

    /**
     *
     * @throws SQLException
     */
    public static void create_schedulesTable() throws SQLException {
        //create schedules table
        String create_scheduleTable =
                "CREATE TABLE IF NOT EXISTS schedules (" +
                        "    scheduleID int NOT NULL auto_increment primary key," +
                        "    billboardID int," +
                        "    scheduledDate Date," +
                        "    startTime Time," +
                        "    endTime Time" +
                        "	);";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_scheduleTable);
        statement.close();
    }

    /**
     *
     * @throws SQLException
     */
    public static void create_usersTable() throws SQLException {
        //create users table
        String create_userInfo =
                "CREATE TABLE IF NOT EXISTS users (" +
                        "    username varchar (256) NOT NULL," +
                        "    password varchar(256) NOT NULL," +
                        "    session varchar(256)," +
                        "    primary key (username)" +
                        "	);";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_userInfo);
        statement.close();
    }

    /**
     *
     * @throws SQLException
     */
    public static void create_permissionsTable() throws SQLException {
        //create permissions table
        String create_permissions ="CREATE TABLE IF NOT EXISTS permissions (" +
                "    username varchar (256) NOT NULL," +
                "    createBillboards tinyint(4)," +
                "    editBillboards tinyint(4)," +
                "    scheduleBillboards tinyint(4)," +
                "    editUsers tinyint(4)," +
                "    primary key (username)" +
                "	);";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_permissions);
        statement.close();
    }

}
