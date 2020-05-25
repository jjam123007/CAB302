package User;

public final class ClientUser {
    private static String token;
    private static String username;
    private static UserPermissions permissions;

    public static String getToken() { return token; }
    public static String getUsername() {return username;}
    public static UserPermissions getPermissions() {return permissions;}

    public ClientUser(String token, String username, UserPermissions permissions){
        this.username = username;
        this.token = token;
        this.permissions = permissions;
    }
}
