package UserManagement;

public final class ClientToken {
    private static String token = "abc";
    public static String getToken() { return token; }
    public ClientToken(String token){
        this.token = token;
    }
}
