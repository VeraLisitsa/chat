package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.net.Socket;

public class ServerTests {
    Server server;

    @BeforeEach
    protected void beforeEach() {
        server = Server.createServer();
    }

    @AfterEach
    protected void afterEach() {
        server = null;
    }

    @Test
    protected void getSettingsTest() {
        int expectedPort = 8089;
        server.getSettings();
        Assertions.assertEquals(expectedPort, server.getPort());
    }

    @Test
    protected void createThreadForUserTest() {
        int expectedCountThreads = server.getListUserThreads().size() + 1;
        Socket clientSocketMock = Mockito.mock(Socket.class);
        server.createThreadForUser(clientSocketMock);
        Assertions.assertEquals(expectedCountThreads, server.getListUserThreads().size());
    }
}

