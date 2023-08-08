package org.example;

import java.io.*;

import static org.example.CurrentTime.getCurrentTime;

public class ClientLog {

    private static ClientLog clientLog;
    private File logFileClient;
    private FileWriter writerLogFileClient;
    private static final String NAME_FILE_LOG_CLIENT = "clientFile.log";


    private ClientLog() {
        createLogFileClient();
    }

    public static ClientLog createClientLog() {
        if (clientLog == null) {
            clientLog = new ClientLog();
        }
        return clientLog;
    }

    public void writeToLogFileClient(String msg) {
        try {
            writerLogFileClient.write(getCurrentTime() + msg + "\n");
            writerLogFileClient.flush();
        } catch (IOException ignore) {
            //NOP

        }
    }

    protected File getFile() {
        return logFileClient;
    }

    protected void createLogFileClient() {
        logFileClient = new File(NAME_FILE_LOG_CLIENT);
        try {
            logFileClient.createNewFile();
            writerLogFileClient = new FileWriter(logFileClient, true);
        } catch (IOException ignore) {
            //NOP
        }
    }
}

