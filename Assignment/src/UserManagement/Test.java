package UserManagement;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws SQLException {
        UserSession.addSession("abc","test");
        System.out.println("Permitted: " +UserSession.hasPermission("abc", PermissionType.createBillboards));
    }
}
