package server;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 34522;

    public static void main(String[] args) {
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));) {
            while (true) {
                try (
                        Socket socket = server.accept(); // accept a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String msgnew = input.readUTF();
                    System.out.println("Received: Give me a record # 12");

                    /**
                     * to accept one message
                     */
                    String msg = input.readUTF(); // read a message from the client
                    output.writeUTF(msg); // resend it to the client
                    System.out.println("Sent: A record # 12 was sent!");
                    return;
                    
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}









