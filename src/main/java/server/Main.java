package server;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import json.GuitarBrand;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
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
                } else if (msg.contains("get")) {
                    database data = new Gson().fromJson(msg, database.class);
                    String key = data.getKey();
                    //JsonElement element = jsonObject.get(key);
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
                    database data = new Gson().fromJson(msg, database.class);
                    String key = data.getKey();
                    boolean isThere = false;
                    if (jsonObjectList.size() >= 1) {
                        for (JsonObject jsonObject : jsonObjectList
                        ) {
                            database dataNew = new Gson().fromJson(jsonObject, database.class);
                            String keyNew = dataNew.getKey();
                            if (Objects.equals(keyNew, key)) {
                                jsonObjectList.remove(jsonObject);
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
                    database data = new Gson().fromJson(msg, database.class);
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
                            }
                        }
                    }
                    if (!isthere) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("key", key);
                        jsonObject.addProperty("value", value);
                        jsonObjectList.add(jsonObject);
                    }
                    database out = new database();
                    out.setResponse("OK");
                    String dataJson = new Gson().toJson(out);
                    output.writeUTF(dataJson);
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
