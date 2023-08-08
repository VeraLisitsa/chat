package org.example;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ClientLogTests {
    ClientLog clientLog;

    @BeforeEach
    protected void beforeEach() {
        clientLog = ClientLog.createClientLog();
    }

    @AfterEach
    protected void afterEach() {
        clientLog = null;
    }

    @Test
    protected void writeToLogFileClientTest() throws IOException {
        String message = "test";
        String expected = message;
        clientLog.writeToLogFileClient(message);
        ReversedLinesFileReader reader = new ReversedLinesFileReader(clientLog.getFile());
        String entry = reader.readLine().replaceAll("\r\n", "");
        Assertions.assertEquals(expected, entry.substring(entry.length() - message.length()));
    }

    @Test
    protected void createLogFileClientTest() {
        clientLog.createLogFileClient();
        Boolean expected = true;
        Assertions.assertEquals(expected, clientLog.getFile().exists());
    }
}

