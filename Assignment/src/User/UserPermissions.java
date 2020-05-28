package User;

import java.io.Serializable;
/**
 * @author Nikolai Taufao | N10481087
 */
public class UserPermissions implements Serializable {
    private boolean createBillboards;
    private boolean editBillboards;
    private boolean scheduleBillboards;
    private boolean editUsers;
    private boolean admin;

    public boolean canCreateBillboards() {
        return createBillboards;
    }
    public boolean canEditBillboards() {
        return editBillboards;
    }
    public boolean canScheduleBillboards() {
        return scheduleBillboards;
    }
    public boolean canEditUsers() {
        return editUsers;
    }
    public boolean isAdmin() { return admin; }

    /**
     * Arrange the permissions associated with a user.
     * @param createBillboards
     * @param editBillboards
     * @param scheduleBillboards
     * @param editUsers
     */
    public UserPermissions(boolean createBillboards, boolean editBillboards, boolean scheduleBillboards, boolean editUsers){
        this.createBillboards = createBillboards;
        this.editBillboards = editBillboards;
        this.scheduleBillboards = scheduleBillboards;
        this.editUsers = editUsers;
        this.admin = (editUsers == true);
    }
}
