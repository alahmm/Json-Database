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
    private static final String address = "192.168.1.29";
    private static final int port = 34522;

    public static void main(String[] args) {
        String[] database = new String[100];
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));) {
            while (true) {
                try (
                        Socket socket = server.accept(); // accept a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String msg = input.readUTF(); // read a message from the client
                    String[] listOfMsg = msg.split("\\s");
                    if (msg.equals("exit")) {
                        return;

                    } else if (msg.split("\\s")[0].equals("get")) {
                        int index = Integer.parseInt(msg.split("\\s")[1]);

                        if (index < 1 || index > 100 ) {
                            output.writeUTF("ERROR");
                        } else {
                            List<database> results = deserializer();

                            boolean isId = false;
                            for (database object : results
                            ) {
                                if (object.getIndex() == index - 1) {
                                    isId = true;
                                    output.writeUTF(object.getText());
                                }
                            }
                            if(!isId)
                            {
                                output.writeUTF("ERROR");
                            }
                        }

                    } else if (msg.split("\\s")[0].equals("delete")) {
                        int index = Integer.parseInt(msg.split("\\s")[1]);
                        if (index < 1 || index > 100 ) {
                            output.writeUTF("ERROR");
                        } else {
                            List<database> results = deserializer();
                            boolean isId = false;
                            for (database object : results
                            ) {
                                if (object.getIndex() == index - 1) {
                                    isId = true;
                                    serializer(index - 1, results);
                                    output.writeUTF("OK");
                                }
                            }
                            if(!isId)
                            {
                                output.writeUTF("ERROR");
                            }
                        }
                    } else if (msg.split("\\s+")[0].equals("set")) {
                        int index = Integer.parseInt(msg.split("\\s")[1]);
                        if (index < 1 || index > 100 ) {
                            output.writeUTF("ERROR");
                        } else {
                            database[index - 1] = "";
                            for (int i = 2; i < listOfMsg.length; i++) {
                                database[index - 1] += listOfMsg[i] + " ";
                            }
                            output.writeUTF("OK");
                            database data = new database();
                            data.setIndex(Integer.parseInt(listOfMsg[1]) - 1);
                            data.setText(database[index - 1]);

                            FileOutputStream fileOutputStream
                                    = new FileOutputStream("data.txt", true);
                            ObjectOutputStream objectOutputStream
                                    = new ObjectOutputStream(fileOutputStream);
                            objectOutputStream.writeObject(data);
                            objectOutputStream.close();

                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<database> deserializer() throws IOException {
        List<database> results = new ArrayList<>();

        FileInputStream fis = null;
        try {
            fis =new FileInputStream("data.txt");
            while (true) {
                ObjectInputStream ois = new ObjectInputStream(fis);
                results.add((database) ois.readObject());
            }
        } catch (EOFException | FileNotFoundException ignored) {

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis != null)
                fis.close();
        }

        return results;
    }
    public static void serializer(int id, List<database> listOfOldContent) throws IOException {
        List<database> listOfNewContent = listOfOldContent.stream().
                filter(data -> data.getIndex() != id).toList();
        new FileOutputStream("data.txt").close();
        if (listOfNewContent.size() >= 1) {
            for (database object : listOfNewContent
            ) {
                FileOutputStream fos = new FileOutputStream("data.txt", true);
                BufferedOutputStream bos = new BufferedOutputStream(fos);//for speeding up the I/O operations
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(object);
                oos.close();
            }
        }
    }

}






