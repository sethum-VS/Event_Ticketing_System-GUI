package com.ticket.ticketsys.pool;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TicketPool {
    private final List<String> tickets = Collections.synchronizedList(new LinkedList<>());
    private int totalTicketsAdded = 0;
    private int totalTicketsRetrieved = 0;
    private int maxTicketCapacity;
    private int vendorCount;

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition canAddTickets = lock.newCondition();

    public void initialize(int totalTickets, int maxTicketCapacity, int vendorCount) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.vendorCount = vendorCount;
        this.totalTicketsAdded = Math.min(totalTickets, maxTicketCapacity);
        for (int i = 0; i < this.totalTicketsAdded; i++) {
            tickets.add("Initial-Ticket-" + (i + 1));
        }
    }

    public void addTicket(String ticket) throws InterruptedException {
        lock.lock();
        try {
            while (tickets.size() >= maxTicketCapacity) {
                canAddTickets.await();
            }
            tickets.add(ticket);
            totalTicketsAdded++;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String removeTicket() throws InterruptedException {
        lock.lock();
        try {
            while (tickets.isEmpty()) {
                notEmpty.await();
            }
            String ticket = tickets.remove(0);
            totalTicketsRetrieved++;
            canAddTickets.signalAll();
            return ticket;
        } finally {
            lock.unlock();
        }
    }

    public int getAvailableTickets() {
        return tickets.size();
    }

    public int getTotalTicketsAdded() {
        return totalTicketsAdded;
    }

    public int getTotalTicketsRetrieved() {
        return totalTicketsRetrieved;
    }
}
