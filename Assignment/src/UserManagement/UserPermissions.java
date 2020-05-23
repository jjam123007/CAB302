package UserManagement;

import java.io.Serializable;
import java.util.Map;

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

    public UserPermissions(boolean createBillboards, boolean editBillboards, boolean scheduleBillboards, boolean editUsers){
        this.createBillboards = createBillboards;
        this.editBillboards = editBillboards;
        this.scheduleBillboards = scheduleBillboards;
        this.editUsers = editUsers;
        this.admin = (editUsers == true);
    }
}
