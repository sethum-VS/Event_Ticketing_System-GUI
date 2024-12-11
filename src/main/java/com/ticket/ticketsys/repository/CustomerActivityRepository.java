package com.ticket.ticketsys.repository;

import com.ticket.ticketsys.entity.CustomerActivity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerActivityRepository extends MongoRepository<CustomerActivity, String> {
}
