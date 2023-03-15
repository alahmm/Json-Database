package client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main{

    private static final String SERVER_ADDRESS = "192.168.1.29";//you can also set adress of other computer if the server is in another machine
    private static final int SERVER_PORT = 34522;
    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("\\n");
            String option;
            while(true) {
                option = scanner.next();
                if (option.equals("exit")) {
                    output.writeUTF(option);
                    return;
                } else {
                    output.writeUTF(option);
                    System.out.println(input.readUTF());
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
