package User;

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
    public ServerUser(String username, Long lastSessionUseTime) {
        this.username = username;
        this.lastSessionUseTime = lastSessionUseTime;
    }
}
