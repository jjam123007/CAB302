
import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import Billboard.ManageBillboards;
import BillboardViewer.Replies.QueryXML;
import BillboardViewer.Requests.ViewerRequest;
import BillboardViewer.Requests.ViewerRequestType;
import ControlPanel.SerializeArray;
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
import java.sql.Statement;

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
        String token = (billboardRequest).getSessionToken();
        Object[] billboard = (billboardRequest).getData();
        switch (request) {
            case addBillboard: {
                try {
                    ManageBillboards.addBillboard(billboard, token);
                    BillboardReply message = new BillboardReply("Success");
                    oos.writeObject(message);
                    oos.flush();
                }catch (SQLException e){
                    BillboardReply message = new BillboardReply("Failure, please check inputs are valid");
                    oos.writeObject(message);
                    oos.flush();
                }
                break;
            }
            case addView: {
                try{
                    ManageBillboards.addView(billboard);
                    BillboardReply message = new BillboardReply("Success");
                    oos.writeObject(message);
                    oos.flush();
                }catch (SQLException e){
                    BillboardReply message = new BillboardReply("Please ensure the input are in correct format and valid \n"+
                            "Date: yyyy-mm-dd \n"+
                            "Time: hh:mm:ss");
                    oos.writeObject(message);
                    oos.flush();
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
                    ManageBillboards.delete(billboard);
                    BillboardReply message = new BillboardReply("Success");
                    oos.writeObject(message);
                    oos.flush();
                }catch (NullPointerException e){
                    BillboardReply message = new BillboardReply("No rows was selected");
                    oos.writeObject(message);
                    oos.flush();
                }
                break;
            }
            case edit: {
                try {
                    ManageBillboards.edit(billboard);
                    BillboardReply message = new BillboardReply("Success");
                    oos.writeObject(message);
                    oos.flush();
                }catch(SQLException e){
                    BillboardReply message = new BillboardReply("Please ensure the inputs are valid");
                    oos.writeObject(message);
                    oos.flush();
                }
                break;
            }

        }
    }

    private static void handleLoginRequest(LoginRequest loginRequest) throws SQLException, IOException {
        LoginReply loginReply = new LoginReply(loginRequest);
        oos.writeObject(loginReply);
        oos.flush();
    }

    private static void handleUserManagementRequest(UserManagementRequest request) throws SQLException, NoSuchAlgorithmException, IOException {
        UserManagementRequestType requestType = request.getRequestType();
        String sessionToken = request.getSessionToken();
        switch (requestType){
            case register:{
                RegisterRequest registerRequest = (RegisterRequest) request.getRequest();
                RegisterReply registerReply = new RegisterReply(registerRequest, sessionToken);
                oos.writeObject(registerReply);
                oos.flush();
                break;
            }
            case getUsernames:{
                ViewUsersReply viewUsersReply = new ViewUsersReply(sessionToken);
                oos.writeObject(viewUsersReply);
                oos.flush();
                break;
            }

            case getPermissions:{
                String username = (String) request.getRequest();
                ViewUserPermissionsReply viewUserPermissionsReply = new ViewUserPermissionsReply(username, sessionToken);
                oos.writeObject(viewUserPermissionsReply);
                oos.flush();
                break;
            }

            case remove:{
                String userToDelete = (String) request.getRequest();
                RemoveUserReply removeUserReply = new RemoveUserReply(userToDelete,sessionToken);
                oos.writeObject(removeUserReply);
                oos.flush();
                break;
            }

            case changePermissions:{
                EditUserPropertyRequest editUserPropertyRequest = (EditUserPropertyRequest) request.getRequest();
                EditUserPermissionsReply editUserPermissionsReply = new EditUserPermissionsReply(editUserPropertyRequest, sessionToken);
                oos.writeObject(editUserPermissionsReply);
                oos.flush();
                break;
            }

            case changePassword:{
                EditUserPropertyRequest editUserPropertyRequest = (EditUserPropertyRequest) request.getRequest();
                ChangeUserPasswordReply changeUserPasswordReply = new ChangeUserPasswordReply(editUserPropertyRequest, sessionToken);
                oos.writeObject(changeUserPasswordReply);
                oos.flush();
                break;
            }

            case logout:{
                LogoutReply logoutReply = new LogoutReply(sessionToken);
                oos.writeObject(logoutReply);
                oos.flush();
                break;
            }
        }
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





