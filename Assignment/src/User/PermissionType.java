package User;

import java.io.Serializable;

/**
 * The permissions a user may have.
 * @author Nikolai Taufao | N10481087
 */
public final class PermissionType implements Serializable {

    public static String createBillboards = "createBillboards";
    public static String editBillboards = "editBillboards";
    public static String scheduleBillboards = "scheduleBillboards";
    public static String editUsers = "editUsers";
}
