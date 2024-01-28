package org.example.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {

    private static Server server;
    private static int port;
    private ServerLog serverLog;

    private static ServerSocket serverSocket;
    protected static List<UserThread> listUserThreads = new ArrayList<>();

    private Server() {
    }

    public static Server createServer() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public void start() {
        getSettings();
        serverLog = ServerLog.createServerLog();
        serverLog.writeToLogFileServer("Server start");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error creating ServerSocket");
            serverLog.writeToLogFileServer("WARNING.Error creating ServerSocket");
        }

        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                createThreadForUser(clientSocket);
            } catch (IOException e) {
                System.out.println("Error creating client socket");
                serverLog.writeToLogFileServer("WARNING.Error creating client socket");
            }
        }
    }

    public void createThreadForUser(Socket clientSocket) {
        UserThread clientInThread = new UserThread(clientSocket);
        listUserThreads.add(clientInThread);
        Thread thread = new Thread(clientInThread);
        thread.start();
    }

    public void getSettings() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("settings.json"));
            JSONObject jsonObject = (JSONObject) obj;
            port = Integer.parseInt((String) jsonObject.get("port"));
        } catch (IOException | ParseException e) {
            new RuntimeException("Error loading server settings");
        }
    }

    public int getPort() {
        return port;
    }

    public List<UserThread> getListUserThreads() {
        return listUserThreads;
    }
}
