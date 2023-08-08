package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String nickName;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientLog clientLog;
    private Scanner scanner;


    private String host;
    private int port;


    Boolean isConnection = true;

    public void runClient() {
        getSettings();
        setClientLog();

        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            createNickName(out, clientLog);

            Thread threadForAnswers = new Thread(() -> {
                getAnswers(in);
            });
            threadForAnswers.start();
            sendMessage(out);

        } catch (IOException e) {
            System.out.println("Sorry. Server is not available");
            clientLog.writeToLogFileClient("WARNING.Error server connection");
        } finally {
            outClose(out);
            inClose(in);
            socketClose(socket);
            scanner.close();
        }
    }

    protected void outClose(PrintWriter out) {
        if (out != null) {
            out.close();
        }
    }

    protected void inClose(BufferedReader in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                //NOP
            }
        }
    }

    protected void socketClose(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getAnswers(BufferedReader in) {
        String answer;
        while (isConnection) {
            try {
                answer = in.readLine();
                if (answer.equals("/exit")) {
                    break;
                }
                System.out.println(answer);
                clientLog.writeToLogFileClient("[" + nickName + "] " + answer);

            } catch (IOException e) {
                isConnection = false;
                clientLog.writeToLogFileClient("WARNING.CError server connection");
                socketClose(socket);
                throw new RuntimeException("Server is not available");
            }
        }
    }

    protected void getSettings() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("settings.json"));
            JSONObject jsonObject = (JSONObject) obj;
            port = Integer.parseInt((String) jsonObject.get("port"));
            host = (String) jsonObject.get("host");
        } catch (IOException | ParseException e) {
            new RuntimeException("Error loading application settings");
        }
    }

    protected int getPort() {
        return port;
    }

    protected String getHost() {
        return host;
    }

    protected String getNickName() {
        return nickName;
    }

    protected void setClientLog() {
        clientLog = ClientLog.createClientLog();
    }

    protected void createNickName(PrintWriter out, ClientLog clientLog) {
        System.out.println("Enter your nickname");
        scanner = new Scanner(System.in);
        nickName = scanner.nextLine();
        out.println(nickName);
        clientLog.writeToLogFileClient("New user join. NickName = " + nickName);
    }

    protected void sendMessage(PrintWriter out) {
        scanner = new Scanner(System.in);
        String message;
        while (true) {
            message = scanner.nextLine();
            if (isConnection) {
                out.println(message);
                if (message.equals("/exit")) {
                    clientLog.writeToLogFileClient("User nickName = " + nickName + " leave chat");
                    break;
                }
            } else {
                System.out.println("Sorry.Server is not available");
                throw new RuntimeException("Server is not available");
            }
        }

    }
}