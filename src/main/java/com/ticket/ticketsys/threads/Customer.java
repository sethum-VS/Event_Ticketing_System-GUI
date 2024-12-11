package com.ticket.ticketsys.threads;

import com.ticket.ticketsys.pool.TicketPool;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalInterval;
    private final int customerId; // New parameter

    public Customer(TicketPool ticketPool, int retrievalInterval, int customerId) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.customerId = customerId; // Initialize the new field
    }

    @Override
    public void run() {
        try {
            while (true) {
                String ticket = ticketPool.removeTicket();
                System.out.println("Customer " + customerId + " retrieved: " + ticket);
                Thread.sleep(retrievalInterval);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

