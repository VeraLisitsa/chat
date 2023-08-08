package org.example;

public class ServerStart {
    public static void main(String[] args) {
        Server server = Server.createServer();
        server.start();
    }
}
