package com.ticket.ticketsys.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer_activities")
public class CustomerActivity {

    @Id
    private String id; // Auto-generated MongoDB identifier
    private int customerId; // ID of the customer performing the action
    private String ticketDetails; // Details of the ticket retrieved or action taken
    private String timestamp; // Action timestamp
    private int customerRetrievalInterval;

    public CustomerActivity(String id, int customerId, String ticketDetails, String timestamp) {
        this.id = id;
        this.customerId = customerId;
        this.ticketDetails = ticketDetails;
        this.timestamp = timestamp;
    }

    public CustomerActivity() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTicketDetails() {
        return ticketDetails;
    }

    public void setTicketDetails(String ticketDetails) {
        this.ticketDetails = ticketDetails;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

