package Database;

import Billboard.BillboardDataSource;
import UserManagement.DataSecurity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;

/**
 * This class used to connect database,
 * and check if there are tables existed in this database,
 * and create account for new user.
 * @author Haoze He(n10100351)
 */
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

    /**
     *This function used to check if there is a table existed,
     * if the table existed, connect to the database and wait for request of users,
     * if the table did not exist, create table in database,
     * then connect to the database and wait for the request of users.
     *
     * @throws SQLException
     */
    public static void checkTableExists() throws SQLException {
        //check if required tables exists
        Connection conn = DBConnection.getInstance();
        DatabaseMetaData md = conn.getMetaData();
        boolean checkBBInfoTable = (md.getTables(null, null, "billboards_info", null).next());
        boolean checkBillboardsTable = (md.getTables(null, null, "billboards", null).next());
        boolean checkSchedulesTable= (md.getTables(null, null, "schedules", null).next());
        boolean checkUserTable = (md.getTables(null, null, "users", null).next());
        boolean checkPermissionsTable = (md.getTables(null, null, "permissions", null).next());

        //if did not exist viewTable, create viewTable
        if(!checkBBInfoTable){
            BillboardDataSource.create_viewTable();
        }
        //if did not exist billboardTable, create billboardTable
        if(!checkBillboardsTable){
            BillboardDataSource.create_billboardTable();
        }
        //if did not exist schedulesTable, create schedulesTable
        if(!checkSchedulesTable){
            BillboardDataSource.create_schedulesTable();
        }
        //if did not exist usersTable, create usersTable
        if(!checkUserTable){
            BillboardDataSource.create_usersTable();
        }
        //if did not exist permissionsTable,create permissionsTable
        if(!checkPermissionsTable){
            BillboardDataSource.create_permissionsTable();
        }
    }

    /**
     * This function used to create new account for new user
     * @author Jun Chen(n10240977)
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public static void createAccount() throws SQLException, NoSuchAlgorithmException {


        try{
            Connection conn = DBConnection.getInstance();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) from users where username='admin';");
            ResultSet rs2 = statement.executeQuery("SELECT * from permissions where username ='admin';");
            // checking if ResultSet is empty. if false, create admin and permissions
            if (rs.next()==false || rs2.next()==false){
                String username = "admin";
                String password = "12345678";
                String saltpw = DataSecurity.hash(password);
                String salt = DataSecurity.randomString();
                String saltedPassword = DataSecurity.hash(saltpw+salt);
                String registerQuery = "INSERT INTO users values('"+username+"', '"+saltedPassword+"', '"+salt+"');";
                String registerPermissionsQuery = "INSERT INTO permissions values('"+username+"', "+"'1','1','1','1');";
                statement.executeQuery(registerQuery);
                statement.executeQuery(registerPermissionsQuery);
            }
        }catch (SQLException | NoSuchAlgorithmException e){
            e.printStackTrace();
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