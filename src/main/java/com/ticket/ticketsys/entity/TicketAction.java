package com.ticket.ticketsys.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ticket_actions")
public class TicketAction {
    @Id
    private String id;
    private String action; // ADD or REMOVE
    private String ticketDetails;
    private int userId; // Vendor ID or Customer ID

    public TicketAction(String id, String action, String ticketDetails, int userId) {
        this.id = id;
        this.action = action;
        this.ticketDetails = ticketDetails;
        this.userId = userId;
    }

    // Getters and Setters
}
