package com.ticket.ticketsys.service;

import com.ticket.ticketsys.entity.LogEntry;
import com.ticket.ticketsys.entity.TicketAction;
import com.ticket.ticketsys.repository.LogRepository;
import com.ticket.ticketsys.repository.TicketActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LoggerService {

    @Autowired
    private LogRepository repository;

    public void log(String message) {
        repository.save(new LogEntry(null, message, LocalDateTime.now().toString()));
    }

    public void error(String message) {
        repository.save(new LogEntry(null, "ERROR: " + message, LocalDateTime.now().toString()));
    }

    public List<LogEntry> getAllLogs() {
        return repository.findAll();
    }

}
