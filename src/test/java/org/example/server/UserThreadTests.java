package org.example.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;

public class UserThreadTests {
    UserThread userThread;
    UserThread userThread1;
    Socket clientSocketMock;
    Server server;

    @BeforeEach
    protected void beforeEach() {
        clientSocketMock = Mockito.mock(Socket.class);
        userThread = new UserThread(clientSocketMock);
        userThread1 = new UserThread(clientSocketMock);
        server = Server.createServer();
        server.getListUserThreads().add(userThread);
        server.getListUserThreads().add(userThread1);
    }

    @AfterEach
    protected void afterEach() {
        server.getListUserThreads().clear();
        userThread = null;
        userThread1 = null;
        clientSocketMock = null;
        server = null;
    }

    @Test
    protected void sendAllClientsTest() throws IOException {
        int expectedSizeListUserThreads = 2;

        Mockito.when(clientSocketMock.getOutputStream())
                .thenReturn(new OutputStream() {
                                @Override
                                public void write(int b) throws IOException {
                                }
                            }
                );

        userThread.setOut(clientSocketMock);
        userThread1.setOut(clientSocketMock);

        int result = userThread.sendAllClients("test");

        Assertions.assertEquals(expectedSizeListUserThreads, result);
    }

    @Test
    protected void setOutTest() throws IOException {
        Mockito.when(clientSocketMock.getOutputStream())
                .thenReturn(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                    }
                });
        userThread.setOut(clientSocketMock);
        Assertions.assertNotNull(userThread.getOut());
    }

    @Test
    protected void setInTest() throws IOException {
        Mockito.when(clientSocketMock.getInputStream())
                .thenReturn(new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return 0;
                    }
                });
        userThread.setIn(clientSocketMock);
        Assertions.assertNotNull(userThread.getIn());
    }

    @Test
    protected void userLeaveChatTest() throws IOException {
        int expectedSizeListUserThreads = 1;
        boolean expectedIsUserLeaveChat = true;

        BufferedReader inMock = Mockito.mock(BufferedReader.class);
        Mockito.when(inMock.readLine())
                .thenReturn("test");

        Mockito.when(clientSocketMock.getOutputStream())
                .thenReturn(new OutputStream() {
                                @Override
                                public void write(int b) throws IOException {
                                }
                            }
                );
        userThread.setOut(clientSocketMock);
        userThread1.setOut(clientSocketMock);
        userThread.setServerLog();
        userThread1.setServerLog();

        userThread.userLeaveChat();

        Assertions.assertEquals(expectedSizeListUserThreads, server.getListUserThreads().size());
        Assertions.assertEquals(expectedIsUserLeaveChat, userThread.getisUserLeaveChat());
    }

    @Test
    protected void readMessageTest() throws IOException {
        BufferedReader inMock = Mockito.mock(BufferedReader.class);
        Mockito.when(inMock.readLine())
                .thenReturn("/exit");

        Mockito.when(clientSocketMock.getOutputStream())
                .thenReturn(new OutputStream() {
                                @Override
                                public void write(int b) throws IOException {
                                }
                            }
                );
        userThread.setOut(clientSocketMock);
        userThread1.setOut(clientSocketMock);
        userThread.setServerLog();
        userThread1.setServerLog();

        PrintWriter outMock = Mockito.mock(PrintWriter.class);
        userThread.readMessage(inMock, outMock);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(outMock).println(argumentCaptor.capture());

        Assertions.assertEquals("/exit", argumentCaptor.getValue());
    }

    @Test
    protected void inCloseNotNullTest() throws IOException {
        BufferedReader inMock = Mockito.mock(BufferedReader.class);
        userThread.inClose(inMock);
        Mockito.verify(inMock, Mockito.times(1)).close();
    }

    @Test
    protected void inCloseNullTest() throws IOException {
        BufferedReader inMock = Mockito.mock(BufferedReader.class);
        userThread.inClose(null);
        Mockito.verify(inMock, Mockito.times(0)).close();
    }

    @Test
    protected void outCloseNotNullTest() throws IOException {
        PrintWriter outMock = Mockito.mock(PrintWriter.class);
        userThread.outClose(outMock);
        Mockito.verify(outMock, Mockito.times(1)).close();
    }

    @Test
    protected void outCloseNullTest() throws IOException {
        PrintWriter outMock = Mockito.mock(PrintWriter.class);
        userThread.outClose(null);
        Mockito.verify(outMock, Mockito.times(0)).close();
    }

    @Test
    protected void clientSocketCloseNotNullTest() throws IOException {
        Socket socketMock = Mockito.mock(Socket.class);
        userThread.clientSocketClose(socketMock);
        Mockito.verify(socketMock, Mockito.times(1)).close();
    }

    @Test
    protected void clientSocketCloseNullTest() throws IOException {
        Socket socketMock = Mockito.mock(Socket.class);
        userThread.clientSocketClose(null);
        Mockito.verify(socketMock, Mockito.times(0)).close();
    }
}
