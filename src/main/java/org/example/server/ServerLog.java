package org.example.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.example.util.CurrentTime.getCurrentTime;

public class ServerLog {
    private static ServerLog serverLog;
    private File logFileServer;
    private FileWriter writerLogFileServer;
    private static final String NAME_LOG_FILE_SERVER = "serverFile.log";

    private ServerLog() {
        createLogFileServer();
    }

    public static ServerLog createServerLog() {
        if (serverLog == null) {
            serverLog = new ServerLog();
        }
        return serverLog;
    }

    public void writeToLogFileServer(String message) {
        try {
            writerLogFileServer.write(getCurrentTime() + message + "\n");
            writerLogFileServer.flush();
        } catch (IOException e) {
            System.out.println("WARNING.Entry not added");
        }
    }

    public void createLogFileServer() {
        logFileServer = new File(NAME_LOG_FILE_SERVER);
        try {
            logFileServer.createNewFile();
            writerLogFileServer = new FileWriter(logFileServer, true);
        } catch (IOException e) {
            System.out.println("Error creating log file");
        }
    }

    public File getFile() {
        return logFileServer;
    }
}

