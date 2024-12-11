package com.ticket.ticketsys.repository;

import com.ticket.ticketsys.entity.TicketAction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketActionRepository extends MongoRepository<TicketAction, String> {}
