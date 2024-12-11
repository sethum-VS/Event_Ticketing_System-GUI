package com.ticket.ticketsys.repository;

import com.ticket.ticketsys.entity.VendorActivity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendorActivityRepository extends MongoRepository<VendorActivity, String> {
}
