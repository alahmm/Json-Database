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

    public static void main(String[] args) {
        String[] listOfData = new String[100];
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\n");
        String msg;
        while(true) {
            msg = scanner.next();
            String[] listOfMsg = msg.split("\\s");
            if (msg.equals("exit")) {
                return;

            } else if (listOfMsg[0].equals("get")) {
                int index = Integer.parseInt(msg.split("\\s")[1]);

                if (index < 1 || index > 100 || listOfData[index - 1] == null) {
                    System.out.println("ERROR");
                } else {
                    System.out.println(listOfData[index - 1]);

                }

            } else if (listOfMsg[0].equals("delete")) {
                int index = Integer.parseInt(listOfMsg[1]);
                if (index < 1 || index > 100 ) {
                    System.out.println("ERROR");
                } else {
                    listOfData[index - 1] = null;
                    System.out.println("OK");
                }
            } else if (listOfMsg[0].equals("set")) {
                int index = Integer.parseInt(listOfMsg[1]);
                if (index < 1 || index > 100 ) {
                    System.out.println("ERROR");
                } else {
                    listOfData[index - 1] = "";
                    for (int i = 2; i < listOfMsg.length; i++) {
                        listOfData[index - 1] += listOfMsg[i] + " ";
                    }
                    System.out.println("OK");

                }
            }
        }

    }
}









