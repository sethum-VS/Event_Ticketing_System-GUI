package com.ticket.ticketsys.repository;

import com.ticket.ticketsys.entity.LogEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<LogEntry, String> {
}
