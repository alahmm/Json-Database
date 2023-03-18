package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.*;
import java.net.Socket;

class FileArgs {

    @Parameter (names = "-t", description = "type", required = true)
    public String type;

    @Parameter(names = "-i", description = "index")
    public String index;

    @Parameter(names = "-m", description = "message")
    public String message;
}

public class Main{

    private static final String SERVER_ADDRESS = "127.0.0.1";//you can also set adress of other computer if the server is in another machine
    private static final int SERVER_PORT = 34523;
    public static void main(String[] args) throws InterruptedException {
        //Thread.sleep(1000);
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            FileArgs fileArgs = new FileArgs();
            JCommander parameters = JCommander.newBuilder()
                    .addObject(fileArgs)
                    .build();
            parameters.parse(args);
            if (fileArgs.type.equals("exit")) {
                output.writeUTF("exit");
                System.out.println("Sent: exit");
                System.out.printf("%nReceived: %s%n", input.readUTF());

            } else if (fileArgs.type.equals("get") || fileArgs.type.equals("delete")) {
                output.writeUTF(fileArgs.type);
                output.writeUTF(fileArgs.index);
                System.out.printf("Sent: %s %s", fileArgs.type, fileArgs.index);
                String answer = input.readUTF();
                System.out.printf("%nReceived: %s%n", answer);
            } else {
                output.writeUTF(fileArgs.type);
                output.writeUTF(String.valueOf(fileArgs.index));
                output.writeUTF(fileArgs.message);

                System.out.printf("Sent: %s %s %s", fileArgs.type, fileArgs.index, fileArgs.message);


                System.out.printf("%nReceived: %s%n", input.readUTF());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}