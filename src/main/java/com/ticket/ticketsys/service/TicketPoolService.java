package com.ticket.ticketsys.service;

import com.ticket.ticketsys.entity.TicketAction;
import com.ticket.ticketsys.repository.TicketActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TicketPoolService {

    private final List<String> tickets = Collections.synchronizedList(new LinkedList<>());
    private int totalTicketsAdded = 0;
    private int totalTicketsRetrieved = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition canAddTickets = lock.newCondition();
    private int vendorsFinished = 0;

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    private int maxTicketCapacity;
    private final int totalTickets; // Real-time pool capacity
    private final int vendorCount;

    @Autowired
    private TicketActionRepository ticketActionRepository;

    public TicketPoolService() {
        this.totalTickets = 0; // Default values to avoid uninitialized properties
        this.maxTicketCapacity = 0;
        this.vendorCount = 0;
    }

    public TicketPoolService(int totalTickets, int maxTicketCapacity, int vendorCount) {
        this.totalTickets = totalTickets;
        this.maxTicketCapacity = maxTicketCapacity;
        this.vendorCount = vendorCount;

        int initialTickets = Math.min(totalTickets, maxTicketCapacity);
        for (int i = 0; i < initialTickets; i++) {
            int vendorId = (i % vendorCount) + 1; // Distribute tickets among vendors
            tickets.add("Initial-Ticket-" + (i + 1) + " (Vendor-" + vendorId + ")");
        }
        this.totalTicketsAdded = initialTickets;
    }

    public void addTickets(String ticket, int vendorId) {
        lock.lock();
        try {
            while ((tickets.size() >= totalTickets || totalTicketsAdded >= maxTicketCapacity)
                    && totalTicketsRetrieved < maxTicketCapacity) {
                canAddTickets.await();
            }

            if (totalTicketsAdded < maxTicketCapacity && totalTicketsRetrieved < maxTicketCapacity) {
                String ticketInfo = ticket + " (Vendor-" + vendorId + ")";
                tickets.add(ticketInfo);
                totalTicketsAdded++;

                // Save action to database
                ticketActionRepository.save(new TicketAction(null, "ADD", ticketInfo, vendorId));

                notEmpty.signalAll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public String removeTicket(int customerId) {
        lock.lock();
        try {
            while (tickets.isEmpty() && vendorsFinished < vendorCount) {
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }

            if (tickets.isEmpty() && vendorsFinished == vendorCount) {
                return null;
            }

            String ticket = tickets.remove(0);
            totalTicketsRetrieved++;

            // Save action to database
            ticketActionRepository.save(new TicketAction(null, "REMOVE", ticket, customerId));

            canAddTickets.signalAll();
            return ticket;
        } finally {
            lock.unlock();
        }
    }

    public void updateMaxTicketCapacity(int newMaxTicketCapacity) {
        lock.lock();
        try {
            this.maxTicketCapacity += newMaxTicketCapacity;
            vendorsFinished = 0; // Reset for the next run
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int getTotalTicketsRetrieved() {
        return totalTicketsRetrieved;
    }

    public int getTotalTicketsAdded() {
        return totalTicketsAdded;
    }

    public boolean isComplete() {
        lock.lock();
        try {
            return (vendorsFinished == vendorCount || totalTicketsRetrieved >= maxTicketCapacity) && tickets.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public void setVendorFinished() {
        lock.lock();
        try {
            vendorsFinished++;
            if (vendorsFinished == vendorCount) {
                notEmpty.signalAll(); // Notify customers that no more tickets will be added
            }
        } finally {
            lock.unlock();
        }
    }
}
