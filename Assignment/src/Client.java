import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    public  static void main (String [] args) throws IOException {

        Socket socket = new Socket("localhost", 3310);

        OutputStream os = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(inputStream);

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        String userName = "";

        //ame.contentEquals("quit") ) {
            System.out.println("Enter person id");
            int personId = Integer.parseInt(myObj.nextLine());

        System.out.println("Enter person name");
        String personName = myObj.nextLine();

        System.out.println("Enter some number");
        float someNumber = Float.parseFloat(myObj.nextLine());


        System.out.println("Select user id to delete");
        int personToDelete = Integer.parseInt(myObj.nextLine());

//        MyClass myclass = new MyClass(personId,personName,someNumber,personToDelete);
//        oos.writeObject(myclass);
        oos.flush();


        ois.close();
        oos.close();

        socket.close();

    }
}
