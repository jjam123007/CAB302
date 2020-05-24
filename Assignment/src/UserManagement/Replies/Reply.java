package UserManagement.Replies;

import java.io.Serializable;

public class Reply implements Serializable {
    protected boolean success = false;
    protected String errorMessage = null;

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
