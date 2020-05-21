package UserManagement;

public final class SessionToken {
    private static String token = null;
    public static String getToken() { return token; }
    public SessionToken(String token){
        this.token = token;
    }
}
