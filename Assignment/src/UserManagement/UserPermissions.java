package UserManagement;

import java.io.Serializable;
import java.util.Map;

public class UserPermissions implements Serializable {
    private boolean createBillboards;
    private boolean editBillboards;
    private boolean scheduleBillboards;
    private boolean editUsers;

    public boolean CreateBillboards() {
        return createBillboards;
    }
    public boolean EditBillboards() {
        return editBillboards;
    }
    public boolean ScheduleBillboards() {
        return scheduleBillboards;
    }
    public boolean editUsers() {
        return editUsers;
    }

    public UserPermissions(boolean createBillboards, boolean editBillboards, boolean scheduleBillboards, boolean editUsers){
        this.createBillboards = createBillboards;
        this.editBillboards = editBillboards;
        this.scheduleBillboards = scheduleBillboards;
        this.editUsers = editUsers;
    }
}
