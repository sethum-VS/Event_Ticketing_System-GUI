package com.ticket.ticketsys.service;
import com.ticket.ticketsys.repository.LogRepository;

import com.ticket.ticketsys.entity.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LoggerService {
    private final LogRepository logRepository;

    public LoggerService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logInfo(String message) {
        logRepository.save(new LogEntry(null, message, LocalDateTime.now()));
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
    // New method to fetch all logs
    public List<LogEntry> getAllLogs() {
        return logRepository.findAll();
    }

}
