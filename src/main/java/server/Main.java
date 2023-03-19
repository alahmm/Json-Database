package server;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 34523;
    static class Session extends Thread {
        private String[] databaseArray;
        private final Socket socket;

        private final ServerSocket serverSocket;

        public Session(Socket socketForClient, String[] databaseArray, ServerSocket serverSocket) {

            this.socket = socketForClient;
            this.databaseArray = databaseArray;
            this.serverSocket = serverSocket;
        }

        public void run() {
            try (
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {

                String msg = input.readUTF(); // read a message from the client
                if (msg.equals("exit")) {
                    output.writeUTF("OK");
                    serverSocket.close();
                } else if (msg.equals("get")) {
                    int index = Integer.parseInt(input.readUTF());

                    if (index < 1 || index > 1000 || databaseArray[index - 1] == null) {
                        output.writeUTF("ERROR");
                    } else {
                        output.writeUTF(databaseArray[index - 1]);
                    }

                } else if (msg.equals("delete")) {
                    int index = Integer.parseInt(input.readUTF());
                    if (index < 1 || index > 1000 ) {
                        output.writeUTF("ERROR");
                    } else {
                        databaseArray[index - 1] = null;
                        output.writeUTF("OK");

                    }
                } else if (msg.equals("set")) {
                    int index = Integer.parseInt(input.readUTF());
                    if (index < 1 || index > 1000 ) {
                        output.writeUTF("ERROR");
                    } else {
                        databaseArray[index - 1] = input.readUTF();
                        output.writeUTF("OK");

                    }
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String[] databaseArray = new String[1000];
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(port, 100, InetAddress.getByName(address));) {
            while (true) {
                Session session = new Session(server.accept(), databaseArray, server);
                session.start(); // does not block this server thread
            }

        }
    }
}
