package com.ticket.ticketsys.threads;

import com.ticket.ticketsys.service.TicketPoolService;
import com.ticket.ticketsys.service.LoggerService;

public class Vendor implements Runnable {

    private final int vendorId;
    private final int releaseRate; // In milliseconds
    private final TicketPoolService ticketPoolService;
    private final LoggerService loggerService;

    public Vendor(int vendorId, int releaseRate, TicketPoolService ticketPoolService, LoggerService loggerService) {
        this.vendorId = vendorId;
        this.releaseRate = releaseRate;
        this.ticketPoolService = ticketPoolService;
        this.loggerService = loggerService;
    }

    @Override
    public void run() {
        try {
            while (ticketPoolService.canAddTickets()) {
                String ticketId = "Ticket-" + System.nanoTime();
                ticketPoolService.addTicket(ticketId, vendorId);

                loggerService.logInfo("Vendor " + vendorId + " added: " + ticketId);

                Thread.sleep(releaseRate);
            }
            loggerService.logInfo("Vendor " + vendorId + " finished adding tickets.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            loggerService.logError("Vendor " + vendorId + " interrupted.");
        }
    }
}
