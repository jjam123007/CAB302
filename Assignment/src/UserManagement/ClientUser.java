package UserManagement;

public final class ClientUser {
    private static String token;
    private static UserPermissions permissions;

    public static String getToken() { return token; }
    public static UserPermissions getPermissions() {return permissions;}

    public ClientUser(String token, UserPermissions permissions){
        this.token = token;
        this.permissions = permissions;
    }
}
