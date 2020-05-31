
import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import Billboard.ManageBillboards;
import BillboardViewer.Replies.QueryXML;
import BillboardViewer.Requests.ViewerRequest;
import BillboardViewer.Requests.ViewerRequestType;
import ControlPanel.SerializeArray;
import Networking.Reply;
import Networking.ReplyError;
import User.*;
import Database.DBConnection;
import UserManagement.Replies.*;
import UserManagement.Requests.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * Check request from client,
 *      * addBillboard
 *      * if request is addBillboard the project will execute addBillboard request,
 *      * if the addBillboard request is successful, the client will show success message,
 *      * if the addBillboard request is failure, the client will show error message
 *      * that will prompt the user to check inputs are valid,
 *      * addView
 *      * if request is addView the project will execute addView request,
 *      * if the addView request is successful, the client will show success message,
 *      * if the addView request is failure, the client will show error message
 *      * that will prompt the user what is correct format.
 *      * showTable
 *      * if request is showTable the project will call showBillboard()function.
 *      * delete
 *      * if request is delete the project will execute delete request,
 *      * if the delete request is successful, the client will show success message,
 *      * if the delete request is failure, the client will show error message
 *      * that will prompt the user no rows was selected.
 *      * edit
 *      * if request is edit the project will execute edit request,
 *      * if the edit request is successful, the client will show success message,
 *      * if the edit request is failure, the client will show error message
 *      * that will prompt the user inputs are valid.
 * @author Jun Chen(n10240977)&Haoze He(n10100351)
 * @author Nikolai Taufao(n10481087);
 */
public class Server {
    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    /**
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void main (String [] args) throws IOException, ClassNotFoundException, SQLException {
        ServerSocket serverSocket = new ServerSocket(3310);
        DBConnection.checkTableExists();

        for(;;){
            try {
                // Accept request from the client
                setStreams(serverSocket);

                // Read request
                Object request = ois.readObject();
                System.out.println("request received");

                // Handle request
                if (request instanceof LoginRequest)
                {
                    handleLoginRequest((LoginRequest) request);
                }
                else if (request instanceof BillboardRequest)
                {
                    handleBillboardRequests((BillboardRequest) request);
                }
                else if (request instanceof UserManagementRequest){
                    handleUserManagementRequest((UserManagementRequest) request);
                }
                else if (request instanceof ViewerRequest) {
                    handleViewerRequest((ViewerRequest) request);
                }

                // Close the connection
                closeStreams();
            } catch (EOFException | NoSuchAlgorithmException e) {
                System.out.println("Connection closed.");
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param billboardRequest
     * @throws SQLException
     * @throws IOException
     */
    private static void handleBillboardRequests(BillboardRequest billboardRequest) throws SQLException, IOException {
        BillboardRequestType request = (billboardRequest).getRequest();  System.out.println("Request type :"+ request);
        String sessionToken = (billboardRequest).getSessionToken();
        boolean sessionValid = ServerUserSession.isValid(sessionToken);
        Object[] billboard = (billboardRequest).getData();
        BillboardReply replyMessage = null;
        System.out.println(sessionValid);
        if (sessionValid){
            switch (request) {
                case addBillboard: {
                    try {
                        if (ServerUserSession.hasPermission(sessionToken, PermissionType.createBillboards)){
                            ManageBillboards.addBillboard(billboard, sessionToken);
                            replyMessage = new BillboardReply("Success");
                        } else {
                            replyMessage = new BillboardReply(ReplyError.userNotPermitted);
                        }
                    }catch (SQLException e){
                        replyMessage = new BillboardReply("Failure, please check inputs are valid");
                    }
                    break;
                }
                case addView: {
                    try{
                        if (ServerUserSession.hasPermission(sessionToken, PermissionType.scheduleBillboards)){
                            ManageBillboards.addView(billboard);
                            replyMessage = new BillboardReply("Success");
                        } else {
                            replyMessage = new BillboardReply(ReplyError.userNotPermitted);
                        }
                    }catch (SQLException e){
                         replyMessage = new BillboardReply("Please ensure the input are in correct format and valid \n"+
                                "Date: yyyy-mm-dd \n"+
                                "Time: hh:mm:ss");
                    }
                    break;
                }
                case showTable: {
                    SerializeArray tableData = ManageBillboards.showBillboards();
                    oos.writeObject(tableData);
                    oos.flush();
                    break;
                }
                case delete: {
                    try{
                        if (ServerUserSession.hasPermission(sessionToken, PermissionType.editBillboards)){
                            ManageBillboards.delete(billboard);
                            replyMessage = new BillboardReply("Success");
                        } else {
                            replyMessage = new BillboardReply(ReplyError.userNotPermitted);
                        }
                    }catch (NullPointerException e){
                        replyMessage = new BillboardReply("No rows was selected");
                    }
                    break;
                }
                case edit: {
                    try {
                        if (ServerUserSession.hasPermission(sessionToken, PermissionType.editBillboards))
                        {
                            ManageBillboards.edit(billboard);
                            replyMessage = new BillboardReply("Success");
                        } else{
                            replyMessage = new BillboardReply(ReplyError.userNotPermitted);
                        }
                    }catch(SQLException e){
                        replyMessage = new BillboardReply("Please ensure the inputs are valid");
                    }
                    break;
                }
            }
        } else {
            replyMessage = new BillboardReply(ReplyError.expiredSessionToken);
        }
        oos.writeObject(replyMessage);
        oos.flush();
    }


    /**
     * Send a reply back to and notify the control panel client on whether their entered credentials were valid or not.
     * @param loginRequest
     * @throws SQLException
     * @throws IOException
     */
    private static void handleLoginRequest(LoginRequest loginRequest) throws SQLException, IOException {
        LoginReply loginReply = new LoginReply(loginRequest);
        oos.writeObject(loginReply);
        oos.flush();
    }

    /**
     * Handle requests that involve adding, removing, changing the permissions and retrieving the details of users.
     * This method also includes the handling of user session tokens, namely expiring a session token when a user sends a request to log out.
     * @param request
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static void handleUserManagementRequest(UserManagementRequest request) throws SQLException, NoSuchAlgorithmException, IOException {
        UserManagementRequestType requestType = request.getRequestType();
        String sessionToken = request.getSessionToken();
        Object reply = null;

        switch (requestType){
            //Register a new user specified in the request to the database.
            case register:{
                RegisterRequest registerRequest = (RegisterRequest) request.getRequest();
                RegisterReply registerReply = new RegisterReply(registerRequest, sessionToken);
                reply = registerReply;
                break;
            }
            //Retrieve all users registered to the database and send it to the client.
            case getUsernames:{
                ViewUsersReply viewUsersReply = new ViewUsersReply(sessionToken);
                reply = viewUsersReply;
                break;
            }
            //Retrieve the permissions of a user specified in the request and send it to the client.
            case getPermissions:{
                String username = (String) request.getRequest();
                ViewUserPermissionsReply viewUserPermissionsReply = new ViewUserPermissionsReply(username, sessionToken);
                reply = viewUserPermissionsReply;
                break;
            }
            //Remove the user designated in the request from the database.
            case remove:{
                String userToDelete = (String) request.getRequest();
                RemoveUserReply removeUserReply = new RemoveUserReply(userToDelete,sessionToken);
                reply = removeUserReply;
                break;
            }
            //Alter the permissions of a user specified in the request.
            case changePermissions:{
                EditUserPropertyRequest editUserPropertyRequest = (EditUserPropertyRequest) request.getRequest();
                EditUserPermissionsReply editUserPermissionsReply = new EditUserPermissionsReply(editUserPropertyRequest, sessionToken);
                reply = editUserPermissionsReply;
                break;
            }
            //Alter the password of a user specified in the request.
            case changePassword:{
                EditUserPropertyRequest editUserPropertyRequest = (EditUserPropertyRequest) request.getRequest();
                ChangeUserPasswordReply changeUserPasswordReply = new ChangeUserPasswordReply(editUserPropertyRequest, sessionToken);
                reply = changeUserPasswordReply;
                break;
            }
            //Log the user associated with the session token out of the system by expiring their session token.
            case logout:{
                LogoutReply logoutReply = new LogoutReply(sessionToken);
                reply = logoutReply;
                break;
            }
        }

        oos.writeObject(reply);
        oos.flush();
    }

    // Handle requests from billboard viewer
    private static void handleViewerRequest(ViewerRequest request) throws IOException {
        // Get the request type
        ViewerRequestType requestType = request.getRequestType();

        // Act based on its type
        switch (requestType) {
            case getXML: {
                String xml = QueryXML.queryXML();
                oos.writeObject(xml);
                break;
            }
        }
    }

    private static void setStreams(ServerSocket serverSocket) throws IOException {
        socket = serverSocket.accept();
        System.out.println("Connected to "+ socket.getInetAddress());
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream( socket.getInputStream());
    }

    private static void closeStreams() throws IOException {
        ois.close();
        oos.close();
        socket.close();
    }
}





