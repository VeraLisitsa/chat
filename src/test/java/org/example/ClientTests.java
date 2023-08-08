package org.example;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.*;

import java.net.Socket;
import java.net.UnknownHostException;


public class ClientTests {
    Client client;

    @BeforeEach
    protected void beforeEach() {
        client = new Client();
    }

    @AfterEach
    protected void afterEach() {
        client = null;
    }

    @Test
    protected void getSettingsPortTest() {
        int expectedPort = 8089;
        client.getSettings();
        Assertions.assertEquals(expectedPort, client.getPort());
    }

    @Test
    protected void getSettingsHostTest() {
        String expectedHost = "127.0.0.1";
        client.getSettings();
        Assertions.assertEquals(expectedHost, client.getHost());
    }


    @Test
    protected void getAnswersTest() throws IOException {
        client.setClientLog();
        BufferedReader inMock = Mockito.mock(BufferedReader.class);
        Mockito.when(inMock.readLine())
                .thenReturn("test").thenReturn("/exit");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        client.getAnswers(inMock);
        String expected = "test";
        String result = outputStream.toString().replace("\r\n", "");
        Assertions.assertEquals(expected, result);

    }

    @Test
    protected void createNickNameTest() throws UnknownHostException {
        PrintWriter outMock = Mockito.mock(PrintWriter.class);
        ClientLog clientLog = Mockito.mock(ClientLog.class);

        String input = "name", expected = "name";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        client.createNickName(outMock, clientLog);
        Assertions.assertEquals(expected, client.getNickName());

    }

    @Test
    protected void sendMessageTest() {
        client.setClientLog();

        PrintWriter outMock = Mockito.mock(PrintWriter.class);

        String input = "message\n/exit";
        String output1 = "message";
        String output2 = "/exit";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        client.sendMessage(outMock);

        Mockito.verify(outMock, Mockito.times(2)).println(Mockito.anyString());
        Mockito.verify(outMock, Mockito.times(1)).println(output1);
        Mockito.verify(outMock, Mockito.times(1)).println(output2);

    }

    @Test
    protected void inCloseNotNullTest() throws IOException {
        BufferedReader inMock = Mockito.mock(BufferedReader.class);
        client.inClose(inMock);
        Mockito.verify(inMock, Mockito.times(1)).close();
    }

    @Test
    protected void inCloseNullTest() throws IOException {
        BufferedReader inMock = Mockito.mock(BufferedReader.class);
        client.inClose(null);
        Mockito.verify(inMock, Mockito.times(0)).close();
    }

    @Test
    protected void outCloseNotNullTest() throws IOException {
        PrintWriter outMock = Mockito.mock(PrintWriter.class);
        client.outClose(outMock);
        Mockito.verify(outMock, Mockito.times(1)).close();
    }

    @Test
    protected void outCloseNullTest() throws IOException {
        PrintWriter outMock = Mockito.mock(PrintWriter.class);
        client.outClose(null);
        Mockito.verify(outMock, Mockito.times(0)).close();
    }

    @Test
    protected void socketCloseNotNullTest() throws IOException {
        Socket socketMock = Mockito.mock(Socket.class);
        client.socketClose(socketMock);
        Mockito.verify(socketMock, Mockito.times(1)).close();
    }

    @Test
    protected void socketCloseNullTest() throws IOException {
        Socket socketMock = Mockito.mock(Socket.class);
        client.socketClose(null);
        Mockito.verify(socketMock, Mockito.times(0)).close();
    }

}

