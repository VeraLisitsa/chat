package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.example.CurrentTime.getCurrentTime;

public class ServerLog {
    private static ServerLog serverLog;
    private File logFileServer;
    private FileWriter writerLogFileServer;
    private static final String NAME_LOG_FILE_SERVER = "serverFile.log";

    private ServerLog() {
        createLogFileServer();
    }

    protected static ServerLog createServerLog() {
        if (serverLog == null) {
            serverLog = new ServerLog();
        }
        return serverLog;
    }

    protected void writeToLogFileServer(String message) {
        try {
            writerLogFileServer.write(getCurrentTime() + message + "\n");
            writerLogFileServer.flush();
        } catch (IOException e) {
            System.out.println("WARNING.Entry not added");
        }
    }

    protected void createLogFileServer() {
        logFileServer = new File(NAME_LOG_FILE_SERVER);
        try {
            logFileServer.createNewFile();
            writerLogFileServer = new FileWriter(logFileServer, true);
        } catch (IOException e) {
            System.out.println("Error creating log file");
        }
    }

    protected File getFile() {
        return logFileServer;
    }
}

