package com.ticket.ticketsys.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "logs")
public class LogEntry {
    @Id
    private String id;
    private String message;
    private LocalDateTime timestamp;

    public LogEntry(String id, String message, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters
}
