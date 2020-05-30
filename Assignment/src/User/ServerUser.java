package User;
/**
 * @author Nikolai Taufao | N10481087
 */
public class ServerUser {
    public String getUsername() {
        return username;
    }

    public Long getLastSessionUseTime() {
        return lastSessionUseTime;
    }
    public void setLastSessionUseTime(Long lastSessionUseTime) {
        this.lastSessionUseTime = lastSessionUseTime;
    }

    private String username;
    private Long lastSessionUseTime;


    /**
     * Store an instance of a user on the server.
     * @param username
     * @param lastSessionUseTime the last time the user sent their session token.
     */
    public ServerUser(String username, Long lastSessionUseTime) {
        this.username = username;
        this.lastSessionUseTime = lastSessionUseTime;
    }
}
