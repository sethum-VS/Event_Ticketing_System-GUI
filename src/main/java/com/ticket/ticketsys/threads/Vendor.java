package com.ticket.ticketsys.threads;

import com.ticket.ticketsys.pool.TicketPool;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseInterval;
    private final int vendorId;

    public Vendor(TicketPool ticketPool, int releaseInterval, int vendorId) {
        this.ticketPool = ticketPool;
        this.releaseInterval = releaseInterval;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String ticket = "Ticket-" + System.nanoTime() + " (Vendor-" + vendorId + ")";
                ticketPool.addTicket(ticket);
                Thread.sleep(releaseInterval);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
