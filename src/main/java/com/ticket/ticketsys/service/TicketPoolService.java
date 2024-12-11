package com.ticket.ticketsys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TicketPoolService {

    @Autowired
    private LoggerService loggerService;

    private final Queue<String> ticketPool = new LinkedList<>(); // Thread-safe queue for tickets
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition(); // Condition for ticket retrieval
    private final Condition canAdd = lock.newCondition();   // Condition for ticket addition

    private int totalTicketsAdded = 0;
    private int totalTicketsRetrieved = 0;
    private int maxTicketCapacity = 0;
    private int vendorsFinished = 0; // Tracks vendor completion

    /**
     * Initialize the ticket pool with a given number of tickets and max capacity.
     */
    public void initializePool(int totalTickets, int maxTicketCapacity) {
        lock.lock();
        try {
            ticketPool.clear();
            totalTicketsAdded = 0;
            totalTicketsRetrieved = 0;
            vendorsFinished = 0;
            this.maxTicketCapacity = maxTicketCapacity;

            int initialTickets = Math.min(totalTickets, maxTicketCapacity);
            for (int i = 0; i < initialTickets; i++) {
                ticketPool.add("Initial-Ticket-" + (i + 1));
            }
            totalTicketsAdded = initialTickets;

            loggerService.logInfo("Ticket pool initialized with " + totalTicketsAdded + " tickets.");
        } finally {
            lock.unlock();
        }
    }

    /**
     * Add tickets to the pool (vendor action).
     */
    public void addTickets(String ticket) {
        lock.lock();
        try {
            while (totalTicketsAdded >= maxTicketCapacity) {
                canAdd.await(); // Wait until tickets can be added
            }

            ticketPool.add(ticket);
            totalTicketsAdded++;
            notEmpty.signalAll(); // Notify customers that tickets are available

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            loggerService.logInfo("Vendor thread interrupted during addTickets.");
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieve tickets from the pool (customer action).
     */
    public String removeTicket(int customerId) {
        lock.lock();
        try {
            while (ticketPool.isEmpty() && vendorsFinished == 0) {
                notEmpty.await(); // Wait until tickets are available
            }

            if (ticketPool.isEmpty() && vendorsFinished > 0) {
                return null; // No tickets left, and vendors are finished
            }

            String ticket = ticketPool.poll();
            totalTicketsRetrieved++;
            canAdd.signalAll(); // Notify vendors that tickets can be added

            return ticket;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            loggerService.logInfo("Customer thread interrupted during removeTicket.");
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Reset the ticket pool after a simulation.
     */
    public void resetPool() {
        lock.lock();
        try {
            ticketPool.clear();
            totalTicketsAdded = 0;
            totalTicketsRetrieved = 0;
            vendorsFinished = 0;
            loggerService.logInfo("Ticket pool reset.");
        } finally {
            lock.unlock();
        }
    }

    /**
     * Mark a vendor as finished.
     */
    public void setVendorFinished() {
        lock.lock();
        try {
            vendorsFinished++;
            if (vendorsFinished > 0 && ticketPool.isEmpty()) {
                notEmpty.signalAll(); // Notify customers that no more tickets will be added
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Check if tickets can still be added.
     */
    public boolean canAddTickets() {
        lock.lock();
        try {
            return totalTicketsAdded < maxTicketCapacity;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Check if tickets can still be retrieved.
     */
    public boolean canRetrieveTickets() {
        lock.lock();
        try {
            return !ticketPool.isEmpty();
        } finally {
            lock.unlock();
        }
    }
}
