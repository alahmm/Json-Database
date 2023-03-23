package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.database;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;


public class Main{

    static String dirPathToImportFrom = "C:\\Users\\alahmm\\IdeaProjects\\JSON Database\\src\\main\\java\\client\\data\\";


    private static final String SERVER_ADDRESS = "127.0.0.1";//you can also set adress of other computer if the server is in another machine
    private static final int SERVER_PORT = 34523;
    public static void main(String[] args) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("\\n");
            String msg = scanner.next();
            String[] listOfMsg = msg.split("\\s");
            if (listOfMsg[0].equals("exit")) {
                database data = new database();
                data.setType(listOfMsg[0]);
                String dataJson = new Gson().toJson(data);
                output.writeUTF(dataJson);
                System.out.println("Sent: "+ dataJson);
                String answer = input.readUTF();
                System.out.printf("%nReceived: %s%n", answer);

            } else if (listOfMsg[0].equals("get") || listOfMsg[0].equals("delete")) {
                database data = new database();
                data.setType(listOfMsg[0]);
                data.setKey(listOfMsg[1]);
                String dataJson = new Gson().toJson(data);
                output.writeUTF(dataJson);
                System.out.println("Sent: "+ dataJson);
                String answer = input.readUTF();
                System.out.printf("%nReceived: %s%n", answer);
            } else if (listOfMsg[0].equals("-in")) {
                String filename = listOfMsg[1];
                Scanner scannerNew = new Scanner(new File((dirPathToImportFrom + filename)));
                String fileContent = "";
                while (scannerNew.hasNext()) {
                     fileContent = scannerNew.nextLine();
                }
                scannerNew.close();
                output.writeUTF(fileContent);
                System.out.println("Sent: "+ fileContent);
                String answer = input.readUTF();
                System.out.printf("%nReceived: %s%n", answer);

            } else {
                database data = new database();
                data.setType(listOfMsg[0]);
                data.setKey(listOfMsg[1]);
                data.setValue(listOfMsg[2]);
                String dataJson = new Gson().toJson(data);
                output.writeUTF(dataJson);
                System.out.println("Sent: "+ dataJson);
                String answer = input.readUTF();
                System.out.printf("%nReceived: %s%n", answer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}