import java.sql.*;

public class Connect_db {
    static  Connection con=null;

    public static Connection getConnection() {
        if (con != null) return con;
        String db = "jdbc:mariadb://localhost:3306/mybd";
        String user = "root";
        String pass = "";
        return getConnection (db,user,pass);
    }

    private static Connection getConnection(String db_name, String user_name, String password){
        try {
            con = DriverManager.getConnection(db_name,user_name,password);
        } catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }

    public static void closeConnection() throws SQLException {
        con.close();
    }


}

