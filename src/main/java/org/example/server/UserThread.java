package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.example.server.Server.listUserThreads;
import static org.example.util.CurrentTime.getCurrentTime;

public class UserThread implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String nickName;
    private boolean isUserLeaveChat = false;
    private ServerLog serverLog;


    public UserThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            setServerLog();
            setOut(clientSocket);
            setIn(clientSocket);
            nickName = in.readLine();
            serverLog.writeToLogFileServer("New user join. NickName = " + nickName + ", ip = " + clientSocket.getInetAddress() + ", port = " + clientSocket.getPort());
            sendAllClients(nickName + " join to chat");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isUserLeaveChat) {
            readMessage(in, out);
        }
        outClose(out);
        inClose(in);
        clientSocketClose(clientSocket);
    }

    public void inClose(BufferedReader in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                serverLog.writeToLogFileServer("WARNING.Error closing BufferedReader");
            }
        }
    }

    public void outClose(PrintWriter out) {
        if (out != null) {
            out.close();
        }
    }

    public void clientSocketClose(Socket clientSocket) {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            serverLog.writeToLogFileServer("WARNING.Error closing client socket");
        }
    }

    public int sendAllClients(String message) {
        int countMessageRecepient = 0;
        for (UserThread userThread : listUserThreads) {
            userThread.getOut().println(getCurrentTime() + message);
            countMessageRecepient++;
        }
        return countMessageRecepient;
    }

    private void sendAllClientsMessage(String message) {
        sendAllClients("[" + nickName + "] " + message);
    }

    public void userLeaveChat() {
        listUserThreads.remove(this);
        isUserLeaveChat = true;
        sendAllClients(nickName + " leave chat");
        serverLog.writeToLogFileServer("User nickName = " + nickName + " leave chat");
    }

    public void readMessage(BufferedReader in, PrintWriter out) {
        String message;
        try {
            message = in.readLine();
            if (message.equals("/exit")) {
                out.println(message);
                userLeaveChat();
            } else {
                serverLog.writeToLogFileServer("[" + nickName + "] " + message);
                sendAllClientsMessage(message);
            }
        } catch (IOException e) {
            serverLog.writeToLogFileServer("WARNING. Error reading message");
            isUserLeaveChat = true;
        }
    }

    public PrintWriter getOut() {
        return this.out;
    }

    public void setOut(Socket clientSocket) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public BufferedReader getIn() {
        return this.in;
    }

    public void setIn(Socket clientSocket) throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public boolean getisUserLeaveChat() {
        return isUserLeaveChat;
    }

    public void setServerLog() {
        serverLog = ServerLog.createServerLog();
    }

}


