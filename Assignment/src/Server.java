
import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;
import Billboard.ManageBillboards;
import ControlPanel.SerializeArray;
import UserManagement.*;
import Database.DBConnection;

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
                Object requestObject = ois.readObject();
                if (requestObject instanceof BillboardRequest)
                {
                    BillboardRequestType request = ((BillboardRequest) requestObject).getRequest();  System.out.println("Request type :"+ request);
                    String token = ((BillboardRequest) requestObject).getSessionToken();
                    Object[] billboard = ((BillboardRequest) requestObject).getData();

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
                else if (requestObject instanceof LoginRequest)
                {
                    LoginRequest loginRequest = (LoginRequest) requestObject;
                    LoginReply loginReply = new LoginReply(loginRequest);
                    if (loginReply.isSuccess()){
                        String user = loginRequest.getUsername();
                        UserSession.addSession(loginReply.getSessionToken(), user);
                    }
                    oos.writeObject(loginReply);
                }
                else if (requestObject instanceof RegisterRequest)
                {
                    RegisterRequest registerRequest = (RegisterRequest) requestObject;
                    RegisterReply registerReply = new RegisterReply(registerRequest);
                    oos.writeObject(registerReply);
                }

            } catch (EOFException | NoSuchAlgorithmException e) {
                e.printStackTrace();
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





