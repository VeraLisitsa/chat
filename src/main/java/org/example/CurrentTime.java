package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentTime {
    protected static String getCurrentTime() {
        return "(" + DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a")
                .format(LocalDateTime.now()) + ") ";
    }
}
