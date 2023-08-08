package org.example;

import java.io.*;
import java.net.Socket;

import static org.example.CurrentTime.getCurrentTime;
import static org.example.Server.listUserThreads;

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

    protected void inClose(BufferedReader in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                serverLog.writeToLogFileServer("WARNING.Error closing BufferedReader");
            }
        }
    }

    protected void outClose(PrintWriter out) {
        if (out != null) {
            out.close();
        }
    }

    protected void clientSocketClose(Socket clientSocket) {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            serverLog.writeToLogFileServer("WARNING.Error closing client socket");
        }
    }

    protected int sendAllClients(String message) {
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

    protected void userLeaveChat() {
        listUserThreads.remove(this);
        isUserLeaveChat = true;
        sendAllClients(nickName + " leave chat");
        serverLog.writeToLogFileServer("User nickName = " + nickName + " leave chat");
    }

    protected void readMessage(BufferedReader in, PrintWriter out) {
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

    protected PrintWriter getOut() {
        return this.out;
    }

    protected void setOut(Socket clientSocket) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    protected BufferedReader getIn() {
        return this.in;
    }

    protected void setIn(Socket clientSocket) throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    protected boolean getisUserLeaveChat() {
        return isUserLeaveChat;
    }

    protected void setServerLog() {
        serverLog = ServerLog.createServerLog();
    }

}


