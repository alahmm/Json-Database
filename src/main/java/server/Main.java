package server;
import com.google.gson.*;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    static String dirPath = "C:\\Users\\alahmm\\IdeaProjects\\JSON Database\\src\\main\\java\\server\\data\\db.json";
    String dirPathToImportFrom = System.getProperty("user.dir") + File.separator + "src" + File.separator + "client" + File.separator + "data";
    private static final String address = "127.0.0.1";
    private static final int port = 34523;
    static class Session extends Thread {
        private List<JsonObject> jsonObjectList;
        private final Socket socket;

        private final ServerSocket serverSocket;

        public Session(Socket socketForClient, List<JsonObject> jsonObjectList, ServerSocket serverSocket) {

            this.socket = socketForClient;
            this.jsonObjectList = jsonObjectList;
            this.serverSocket = serverSocket;
        }

        public static String parserToGet (JsonArray keys) throws FileNotFoundException {
            JsonObject jsonObject = new JsonParser().parse(new FileReader(dirPath)).getAsJsonObject();
            JsonObject value = jsonObject.getAsJsonObject("value");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                list.add(keys.get(i).toString().substring(1, keys.get(i).toString().length() - 1));
            }
            if (keys.size() == 1) {
                database out = new database();
                out.setResponse("OK");
                out.setValue(value);
                return new Gson().toJson(out);
            } else {
                for (int i = 1; i < keys.size(); i++) {
                    if (i < keys.size() - 1) {
                        value = value.getAsJsonObject(list.get(i));
                    } else {
                        JsonElement jsonElement = value.get(list.get(i));
                        JsonElementData data = new JsonElementData();
                        data.setResponse("OK");
                        data.setValue(jsonElement);
                        String result =new Gson().toJson(data);
                        return result;
                        }
                    }
                }
            return "0";
        }
        public static String parserToDelete (JsonArray keys) throws FileNotFoundException {
            JsonObject jsonObject = new JsonParser().parse(new FileReader(dirPath)).getAsJsonObject();
            JsonObject value = jsonObject.getAsJsonObject("value");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                list.add(keys.get(i).toString().substring(1, keys.get(i).toString().length() - 1));
            }
            list.set(0, "value");
            for (int i = 1; i < keys.size(); i++) {
                if (i < keys.size() - 1) {
                    value = value.getAsJsonObject(list.get(i));
                } else {
                    value.remove(list.get(i));
                    File file = new File(dirPath);
                    try (FileWriter fileWriter = new FileWriter(file, false)) {
                        fileWriter.write(String.valueOf(jsonObject));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            database out = new database();
            out.setResponse("OK");
            return new Gson().toJson(out);
        }
        public static String parserToSet (JsonArray keys, JsonElement valueNew) throws FileNotFoundException {
            JsonObject jsonObject = new JsonParser().parse(new FileReader(dirPath)).getAsJsonObject();
            JsonObject value = jsonObject.getAsJsonObject("value");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                list.add(keys.get(i).toString().substring(1, keys.get(i).toString().length() - 1));
            }
            list.set(0, "value");
            for (int i = 1; i < keys.size(); i++) {
                if (i < keys.size() - 1) {
                    value = value.getAsJsonObject(list.get(i));
                } else {
                    value.remove(list.get(i));
                    value.add(list.get(i), valueNew);
                    File file = new File(dirPath);
                    try (FileWriter fileWriter = new FileWriter(file, false)) {
                        fileWriter.write(String.valueOf(jsonObject));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            database out = new database();
            out.setResponse("OK");
            return new Gson().toJson(out);
        }
        public void run() {
            try (
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {

                String msg = input.readUTF(); // read a message from the client
                if (msg.contains("exit")) {
                    database out = new database();
                    out.setResponse("OK");
                    String dataJson = new Gson().toJson(out);
                    output.writeUTF(dataJson);
                    serverSocket.close();
                } else if (msg.equals("not a command line")) {
                    msg = input.readUTF();

                    if (msg.equals("secondGetFile.json") || msg.equals("getFile.json")) {
                        msg = input.readUTF();
                        SpecialData specialData = new Gson().fromJson(msg, SpecialData.class);

                        JsonArray keysName = specialData.getKey();
                        String sender = parserToGet(keysName);
                        output.writeUTF(sender);
                    } else if (msg.equals("deleteFile.json")) {
                        msg = input.readUTF();
                        SpecialData specialData = new Gson().fromJson(msg, SpecialData.class);

                        JsonArray keysName = specialData.getKey();
                        String sender = parserToDelete(keysName);
                        output.writeUTF(sender);

                    } else  {
                        if (msg.equals("setFile.json")) {
                            msg = input.readUTF();
                            database specialData = new Gson().fromJson(msg, database.class);

                            String key = specialData.getKey();
                            JsonObject value = specialData.getValue();
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("key", key);
                            jsonObject.add("value", value);
                            File file = new File(dirPath);
                            try (FileWriter fileWriter = new FileWriter(file, false)) {
                                fileWriter.write(String.valueOf(jsonObject));

                            }
                            database out = new database();
                            out.setResponse("OK");
                            output.writeUTF(new Gson().toJson(out));
                        } else {
                            msg = input.readUTF();
                            SpecialData specialData = new Gson().fromJson(msg, SpecialData.class);

                            JsonArray keysName = specialData.getKey();
                            JsonElement value = specialData.getValue();
                            String sender = parserToSet(keysName, value);
                            output.writeUTF(sender);
                        }
                    }
                } else {
                     msg = input.readUTF();
                    if (msg.contains("get")) {
                        databaseFromCL data = new Gson().fromJson(msg, databaseFromCL.class);
                        String key = data.getKey();
                        boolean isThere = false;

                        if (jsonObjectList.size() >= 1) {
                            for (JsonObject jsonObject : jsonObjectList
                            ) {
                                database dataNew = new Gson().fromJson(jsonObject, database.class);
                                String keyNew = dataNew.getKey();
                                if (Objects.equals(keyNew, key)) {
                                    isThere = true;
                                    database out = new database();
                                    out.setResponse("OK");
                                    out.setValue(dataNew.getValue());
                                    String dataJson = new Gson().toJson(out);
                                    output.writeUTF(dataJson);
                                }
                            }
                        }
                        if (!isThere) {
                            database out = new database();
                            out.setResponse("ERROR");
                            out.setReason("No such key");
                            String dataJson = new Gson().toJson(out);
                            output.writeUTF(dataJson);
                        }

                    } else if (msg.contains("delete")) {
                        databaseFromCL data = new Gson().fromJson(msg, databaseFromCL.class);
                        String key = data.getKey();
                        boolean isThere = false;
                        if (jsonObjectList.size() >= 1) {
                            for (JsonObject jsonObject : jsonObjectList
                            ) {
                                database dataNew = new Gson().fromJson(jsonObject, database.class);
                                String keyNew = dataNew.getKey();
                                if (Objects.equals(keyNew, key)) {
                                    jsonObjectList.remove(jsonObject);

                                    File file = new File(dirPath);
                                    try (FileWriter fileWriter = new FileWriter(file, false)) {
                                        fileWriter.write(String.valueOf(jsonObjectList));

                                    }
                                    isThere = true;
                                    database out = new database();
                                    out.setResponse("OK");
                                    String dataJson = new Gson().toJson(out);
                                    output.writeUTF(dataJson);
                                }
                            }
                        }
                        if (!isThere) {
                            database out = new database();
                            out.setResponse("ERROR");
                            out.setReason("No such key");
                            String dataJson = new Gson().toJson(out);
                            output.writeUTF(dataJson);
                        }

                    } else if (msg.contains("set")) {
                        databaseFromCL data = new Gson().fromJson(msg, databaseFromCL.class);
                        String key = data.getKey();
                        String value = data.getValue();
                        boolean isthere = false;
                        if (jsonObjectList.size() >= 1) {
                            for (JsonObject jsonObject : jsonObjectList
                            ) {
                                database dataNew = new Gson().fromJson(jsonObject, database.class);
                                String keyNew = dataNew.getKey();
                                if (Objects.equals(keyNew, key)) {
                                    isthere = true;
                                    int index = jsonObjectList.indexOf(jsonObject);
                                    jsonObject.addProperty("key", key);
                                    jsonObject.addProperty("value", value);
                                    jsonObjectList.set(index, jsonObject);
                                    File file = new File(dirPath);
                                    try (FileWriter fileWriter = new FileWriter(file, false)) {
                                        fileWriter.write(String.valueOf(jsonObjectList));

                                    }
                                }
                            }
                        }
                        if (!isthere) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("key", key);
                            jsonObject.addProperty("value", value);
                            jsonObjectList.add(jsonObject);

                            File file = new File(dirPath);
                            try (FileWriter fileWriter = new FileWriter(file, false)) {
                                fileWriter.write(String.valueOf(jsonObjectList));

                            }

                        }
                        database out = new database();
                        out.setResponse("OK");
                        String dataJson = new Gson().toJson(out);
                        output.writeUTF(dataJson);
                    }
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        List<JsonObject> jsonObjectList = new ArrayList<>();
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(port, 100, InetAddress.getByName(address));) {
            while (true) {
                Session session = new Session(server.accept(), jsonObjectList, server);
                session.start(); // does not block this server thread
            }

        }
    }
}
