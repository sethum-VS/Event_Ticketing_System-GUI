package com.ticket.ticketsys.repository;

import com.ticket.ticketsys.entity.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigurationRepository extends MongoRepository<Configuration, String> {
}
