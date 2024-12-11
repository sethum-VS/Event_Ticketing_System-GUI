package com.ticket.ticketsys.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LoggerService {

    public void logInfo(String message) {
        System.out.println(formatLog("INFO", message));
    }

    public void logError(String message) {
        System.err.println(formatLog("ERROR", message));
    }

    private String formatLog(String level, String message) {
        return String.format("[%s] [%s] %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                level,
                message);
    }
}
