

import java.io.*;
import java.net.Socket;


public class Main{

    private static final String SERVER_ADDRESS = "127.0.0.1";//you can also set adress of other computer if the server is in another machine
    private static final int SERVER_PORT = 34522;
    public static void main(String[] args) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            if (args[1].equals("exit")) {
                output.writeUTF("exit");
                System.out.println("Sent: exit");
                System.out.printf("%nReceived: %s%n", input.readUTF());

            } else if (args[1].equals("get") || args[1].equals("delete")) {
                output.writeUTF(args[1]);
                output.writeUTF(args[3]);
                System.out.printf("%nSent: %s %s", args[1], args[3]);

                System.out.printf("%nReceived: %s%n", input.readUTF());
            } else {
                output.writeUTF(args[1]);
                output.writeUTF(args[3]);
                output.writeUTF(args[5]);

                System.out.printf("Sent: %s %s %s", args[1], args[3], args[5]);


                System.out.printf("%nReceived: %s%n", input.readUTF());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}