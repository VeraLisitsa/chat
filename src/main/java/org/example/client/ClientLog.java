package org.example.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.example.util.CurrentTime.getCurrentTime;

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

    public File getFile() {
        return logFileClient;
    }

    public void createLogFileClient() {
        logFileClient = new File(NAME_FILE_LOG_CLIENT);
        try {
            logFileClient.createNewFile();
            writerLogFileClient = new FileWriter(logFileClient, true);
        } catch (IOException ignore) {
            //NOP
        }
    }
}

