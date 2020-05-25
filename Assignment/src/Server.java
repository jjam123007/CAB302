
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import Billboard.ManageBillboards;
import ControlPanel.SerializeArray;
import User.*;
import Database.DBConnection;
import UserManagement.Replies.*;
import UserManagement.Requests.EditUserPropertyRequest;
import UserManagement.Requests.RegisterRequest;
import UserManagement.Requests.UserManagementRequest;
import UserManagement.Requests.UserManagementRequestType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Statement;


public class Server {

    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static void main (String [] args) throws IOException, ClassNotFoundException, SQLException {
        setStreams();

        for(;;){
            try {
                Object request = ois.readObject();
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
            } catch (EOFException | NoSuchAlgorithmException e) {
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
                ManageBillboards.addBillboard(billboard);
                break;
            }
            case addView: {
                ManageBillboards.addView(billboard);
                break;
            }
            case showTable: {
                Object[][] tableData;

                Statement statement = DBConnection.getInstance().createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM view");

                int rowcount = 0;

                if (resultSet.last()) {
                    rowcount = resultSet.getRow();
                    resultSet.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
                }
                tableData = new Object[rowcount][8];

                for (int i = 0; i < rowcount; i++) {
                    resultSet.next();
                    String billboardID = Integer.toString(resultSet.getInt(1));
                    String BillboardName = resultSet.getString(2);
                    String info = resultSet.getString(3);
                    String msg = resultSet.getString(4);
                    String url = resultSet.getString(5);
                    String schduledDate = resultSet.getString(6);
                    Time startTime = resultSet.getTime(7);
                    Time endTime = resultSet.getTime(8);
                    Object[] myString = {billboardID, BillboardName, info, msg, url, schduledDate, startTime, endTime};
                    tableData[i] = myString;
                }
                statement.close();
                SerializeArray d2 = new SerializeArray(tableData);
                oos.writeObject(d2);
                oos.flush();
                break;
            }
            case delete: {
                ManageBillboards.delete(billboard);
                break;
            }
            case edit: {
                ManageBillboards.edit(billboard);
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
                break;
            }
            case getUsernames:{
                ViewUsersReply viewUsersReply = new ViewUsersReply(sessionToken);
                oos.writeObject(viewUsersReply);
                break;
            }

            case getPermissions:{
                String username = (String) request.getRequest();
                ViewUserPermissionsReply viewUserPermissionsReply = new ViewUserPermissionsReply(username, sessionToken);
                oos.writeObject(viewUserPermissionsReply);
                break;
            }

            case remove:{
                String userToDelete = (String) request.getRequest();
                RemoveUserReply removeUserReply = new RemoveUserReply(userToDelete,sessionToken);
                oos.writeObject(removeUserReply);
                break;
            }

            case changePermissions:{
                EditUserPropertyRequest editUserPropertyRequest = (EditUserPropertyRequest) request.getRequest();
                EditUserPermisionsReply editUserPermisionsReply = new EditUserPermisionsReply(editUserPropertyRequest, sessionToken);
                oos.writeObject(editUserPermisionsReply);
            }

            case changePassword:{
                EditUserPropertyRequest editUserPropertyRequest = (EditUserPropertyRequest) request.getRequest();
                ChangeUserPasswordReply changeUserPasswordReply = new ChangeUserPasswordReply(editUserPropertyRequest, sessionToken);
                oos.writeObject(changeUserPasswordReply);
            }

        }

    }

    private static void setStreams() throws IOException {
        ServerSocket serverSocket = new ServerSocket(3310);
        Socket socket = serverSocket.accept();
        System.out.println("Connected to "+ socket.getInetAddress());
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream( socket.getInputStream());
    }

}





