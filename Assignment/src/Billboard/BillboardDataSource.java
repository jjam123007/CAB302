package Billboard;

import Database.DBConnection;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *This class used to create table for store data of billBoard and users
 * @author Jun Chen(n10240977)
 */
public class BillboardDataSource {

    /**
     *This function used to create a billBoardTable,
     * it sets seven title and value types to store data of billBoard in dataBase
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
                "url longtext," +
                "xml longtext" +
                ");";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_billboardTable);
        statement.close();
    }

    /**
     *This function used to crate a viewTable to show and store some billBoard data presented to users in dataBase.
     * @throws SQLException
     */
    public static void create_viewTable() throws SQLException {
        //create billboards_info table for (view)
        String create_viewTable =
                "CREATE TABLE IF NOT EXISTS billboards_info (" +
                        "viewID int NOT NULL primary key," +
                        "billboardName varchar(60)," +
                        "creatorName varchar(60)," +
                        "message varchar(2000)," +
                        "information varchar(2000)," +
                        "url longtext," +
                        "scheduledDate Date," +
                        "startTime Time," +
                        "endTime Time" +
                        ");";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_viewTable);
        statement.close();
    }

    /**
     *This function used to create a schedulesTable to store schedulesData
     * @throws SQLException
     */
    public static void create_schedulesTable() throws SQLException {
        //create schedules table
        String create_scheduleTable =
                "CREATE TABLE IF NOT EXISTS schedules (" +
                        "scheduleID int NOT NULL auto_increment primary key," +
                        "billboardID int," +
                        "scheduledDate Date NOT NULL," +
                        "startTime Time NOT NULL," +
                        "endTime Time NOT NULL" +
                        ");";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_scheduleTable);
        statement.close();
    }

    /**
     *This function used to create a usersTable to store user data
     * @throws SQLException
     */
    public static void create_usersTable() throws SQLException {
        //create users table
        String create_userInfo =
                "CREATE TABLE IF NOT EXISTS users (" +
                        "username varchar (256) NOT NULL," +
                        "password varchar(256) NOT NULL," +
                        "salt varchar(256) NOT NULL," +
                        "primary key (username)" +
                        ");";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_userInfo);
        statement.close();
    }

    /**
     * This function used to create a permissionsTable to store permissions data of users
     * @throws SQLException
     */
    public static void create_permissionsTable() throws SQLException {
        //create permissions table
        String create_permissions ="CREATE TABLE IF NOT EXISTS permissions (" +
                "username varchar (256) NOT NULL," +
                "createBillboards tinyint(4)," +
                "editBillboards tinyint(4)," +
                "scheduleBillboards tinyint(4)," +
                "editUsers tinyint(4)," +
                "primary key (username)" +
                ");";
        Statement statement = DBConnection.getInstance().createStatement();
        statement.executeQuery(create_permissions);
        statement.close();
    }

}
