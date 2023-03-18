package server;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final String address = "192.168.1.29";
    private static final int port = 34522;
    public static void main(String[] args) throws IOException {
        String[] databaseArray = new String[1000];
        System.out.println("Server started!");

            try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));) {
                while (true) {
                    Session session = new Session(server.accept(), databaseArray);
                    session.start(); // does not block this server thread
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }


}
class Session extends Thread {

    private String[] databaseArray;

    private final Socket socket;

    public Session(Socket socketForClient, String[] databaseArray) {

        this.socket = socketForClient;
        this.databaseArray = databaseArray;
    }
    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {

            String msg = input.readUTF(); // read a message from the client
            String[] listOfMsg = msg.split("\\s");
            if (msg.equals("exit")) {
                output.writeUTF("OK");
                System.exit(0);
            } else if (msg.split("\\s")[0].equals("get")) {
                int index = Integer.parseInt(msg.split("\\s")[1]);

                if (index < 1 || index > 1000 || databaseArray[index - 1] == null) {
                    output.writeUTF("ERROR");
                } else {
                    output.writeUTF(databaseArray[index - 1]);
                }

            } else if (msg.split("\\s")[0].equals("delete")) {
                int index = Integer.parseInt(msg.split("\\s")[1]);
                if (index < 1 || index > 1000 ) {
                    output.writeUTF("ERROR");
                } else {
                    databaseArray[index - 1] = null;
                    output.writeUTF("OK");

                }
            } else if (msg.split("\\s+")[0].equals("set")) {
                int index = Integer.parseInt(msg.split("\\s")[1]);
                if (index < 1 || index > 1000 ) {
                    output.writeUTF("ERROR");
                } else {
                    databaseArray[index - 1] = "";
                    for (int i = 2; i < listOfMsg.length; i++) {
                        databaseArray[index - 1] += listOfMsg[i] + " ";
                    }
                    output.writeUTF("OK");

                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
