package Database;

import Billboard.BillboardDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    private static Connection instance = null;

    private DBConnection() {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            System.out.println("Path: " + Paths.get(".").toAbsolutePath().normalize().toString());
            in = new FileInputStream("src/Database/db.props");
            props.load(in);
            in.close();
            // specify the data source, username and password
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            String schema = props.getProperty("jdbc.schema");

            // get a connection
            instance = DriverManager.getConnection(url + "/" + schema, username,
                    password);
        } catch (SQLException sqle) {
            System.out.println("Unable to make connection with DB");
            System.err.println(sqle);
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void checkTableExists() throws SQLException {
        //check if required tables exists
        Connection conn = DBConnection.getInstance();
        DatabaseMetaData md = conn.getMetaData();
        boolean checkBBInfoTable = (md.getTables(null, null, "billboards_info", null).next());
        boolean checkBillboardsTable = (md.getTables(null, null, "billboards", null).next());
        boolean checkSchedulesTable= (md.getTables(null, null, "schedules", null).next());
        boolean checkUserTable = (md.getTables(null, null, "users", null).next());
        boolean checkPermissionsTable = (md.getTables(null, null, "permissions", null).next());

        if(!checkBBInfoTable){
            BillboardDataSource.create_viewTable();
        }else if(!checkBillboardsTable){
            BillboardDataSource.create_billboardTable();
        }else if(!checkSchedulesTable){
            BillboardDataSource.create_schedulesTable();
        }else if(!checkUserTable){
            BillboardDataSource.create_usersTable();
        }else if(!checkPermissionsTable){
            BillboardDataSource.create_permissionsTable();
        }
    }

    /**
     * Provides global access to the singleton instance of the UrlSet.
     *
     * @return a handle to the singleton instance of the UrlSet.
     */
    public static Connection getInstance() {
        if (instance == null) {
            new DBConnection();
        }
        return instance;
    }

}