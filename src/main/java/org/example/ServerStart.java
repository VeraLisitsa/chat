package org.example;

import org.example.server.Server;

public class ServerStart {
    public static void main(String[] args) {
        Server server = Server.createServer();
        server.start();
    }
}
