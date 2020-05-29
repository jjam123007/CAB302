
import Billboard.BillboardReply;
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import Billboard.ManageBillboards;
import BillboardViewer.Replies.QueryXML;
import BillboardViewer.Requests.ViewerRequest;
import BillboardViewer.Requests.ViewerRequestType;
import ControlPanel.SerializeArray;
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


public class Server {

    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static void main (String [] args) throws IOException, ClassNotFoundException, SQLException {
        ServerSocket serverSocket = new ServerSocket(3310);
        for(;;){
            try {
                setStreams(serverSocket);
                Object request = ois.readObject();
                System.out.println("request received");
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
            } catch (EOFException | NoSuchAlgorithmException e) {
                System.out.println("Connection closed.");
                e.printStackTrace();
            }




        }
    }

    private static void handleBillboardRequests(BillboardRequest billboardRequest) throws SQLException, IOException {
        BillboardRequestType request = (billboardRequest).getRequest();  System.out.println("Request type :"+ request);
        String token = (billboardRequest).getSessionToken();
        Object[] billboard = (billboardRequest).getData();

        switch (request) {
            case addBillboard: {
                try{
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
                EditUserPermissionsReply editUserPermisionsReply = new EditUserPermissionsReply(editUserPropertyRequest, sessionToken);
                oos.writeObject(editUserPermisionsReply);
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
                oos.flush();
                break;
            }
        }
    }

    private static void setStreams(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("Connected to "+ socket.getInetAddress());
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream( socket.getInputStream());
    }

}





