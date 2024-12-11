package com.ticket.ticketsys.threads;

import com.ticket.ticketsys.service.TicketPoolService;
import com.ticket.ticketsys.service.LoggerService;

public class Customer implements Runnable {

    private final int customerId;
    private final int retrievalRate; // In milliseconds
    private final TicketPoolService ticketPoolService;
    private final LoggerService loggerService;

    public Customer(int customerId, int retrievalRate, TicketPoolService ticketPoolService, LoggerService loggerService) {
        this.customerId = customerId;
        this.retrievalRate = retrievalRate;
        this.ticketPoolService = ticketPoolService;
        this.loggerService = loggerService;
    }

    @Override
    public void run() {
        try {
            while (ticketPoolService.canRetrieveTickets()) {
                String ticket = ticketPoolService.removeTicket(customerId);

                if (ticket != null) {
                    loggerService.logInfo("Customer " + customerId + " retrieved: " + ticket);
                } else {
                    loggerService.logInfo("Customer " + customerId + " found no tickets available.");
                }

                Thread.sleep(retrievalRate);
            }
            loggerService.logInfo("Customer " + customerId + " finished retrieving tickets.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            loggerService.logError("Customer " + customerId + " interrupted.");
        }
    }
}
