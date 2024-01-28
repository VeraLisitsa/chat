package org.example.server;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ServerLogTests {
    ServerLog serverLog;

    @BeforeEach
    protected void beforeEach() {
        serverLog = ServerLog.createServerLog();
    }

    @AfterEach
    protected void afterEach() {
        serverLog = null;
    }

    @Test
    protected void writeToLogFileTest() throws IOException {
        String message = "test";
        String expected = message;
        serverLog.writeToLogFileServer(message);
        ReversedLinesFileReader reader = new ReversedLinesFileReader(serverLog.getFile());
        String entry = reader.readLine().replaceAll("\r\n", "");
        Assertions.assertEquals(expected, entry.substring(entry.length() - message.length()));
    }

    @Test
    protected void createLogFileServerTest() {
        serverLog.createLogFileServer();
        Boolean expected = true;
        Assertions.assertEquals(expected, serverLog.getFile().exists());
    }
}

