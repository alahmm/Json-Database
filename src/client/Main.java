package client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main{

    private static final String SERVER_ADDRESS = "127.0.0.1";//you can also set adress of other computer if the server is in another machine
    private static final int SERVER_PORT = 34522;
    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {

            String msg = "Client started!";
            output.writeUTF(msg);
            System.out.println(msg);
            System.out.println("Sent: Give me a record # 12");
            output.writeUTF("Sent: Give me a record # 12");
            String receivedMsg2 = input.readUTF();
            System.out.println("Received: A record # 12 was sent!");



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
