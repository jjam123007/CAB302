package BillboardViewer.Replies;

import Database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class QueryXML {
    public static String queryXML() {
        String query = "Select xml from billboard where BillboardName = \"Taest billboard\";";
        String result = null;
        try {
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet sqlResult = statement.executeQuery(query);
            sqlResult.next();
            result = sqlResult.getString(1);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
