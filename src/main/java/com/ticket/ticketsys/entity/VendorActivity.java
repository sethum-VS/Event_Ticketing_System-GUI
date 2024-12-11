package com.ticket.ticketsys.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vendor_activities")
public class VendorActivity {

    @Id
    private String id;
    private int vendorId;
    private String activity; // e.g., "Added Ticket-1"
    private String timestamp;

    public VendorActivity(String id, int vendorId, String activity, String timestamp) {
        this.id = id;
        this.vendorId = vendorId;
        this.activity = activity;
        this.timestamp = timestamp;
    }

    public VendorActivity() {}

    // Getters and setters
}

