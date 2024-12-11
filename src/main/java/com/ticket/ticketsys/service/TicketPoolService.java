package com.ticket.ticketsys.service;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TicketPoolService {

    private final Queue<String> ticketPool = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition canAdd = lock.newCondition();

    private int totalTicketsAdded = 0;
    private int maxTicketCapacity = 0;

    public void initializePool(int totalTickets, int maxCapacity) {
        lock.lock();
        try {
            maxTicketCapacity = maxCapacity;
            totalTicketsAdded = 0;
            ticketPool.clear();
            for (int i = 0; i < Math.min(totalTickets, maxCapacity); i++) {
                ticketPool.add("Initial-Ticket-" + (i + 1));
                totalTicketsAdded++;
            }
        } finally {
            lock.unlock();
        }
    }

    public void addTicket(String ticket, int vendorId) {
        lock.lock();
        try {
            while (ticketPool.size() >= maxTicketCapacity) {
                canAdd.await();
            }
            ticketPool.add(ticket + " (Vendor-" + vendorId + ")");
            totalTicketsAdded++;
            notEmpty.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public String removeTicket(int customerId) {
        lock.lock();
        try {
            while (ticketPool.isEmpty()) {
                notEmpty.await();
            }
            String ticket = ticketPool.poll();
            canAdd.signalAll();
            return ticket + " (Customer-" + customerId + ")";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public boolean canAddTickets() {
        lock.lock();
        try {
            return totalTicketsAdded < maxTicketCapacity;
        } finally {
            lock.unlock();
        }
    }

    public boolean canRetrieveTickets() {
        lock.lock();
        try {
            return !ticketPool.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public void resetPool() {
        lock.lock();
        try {
            ticketPool.clear();
            totalTicketsAdded = 0;
        } finally {
            lock.unlock();
        }
    }
}
