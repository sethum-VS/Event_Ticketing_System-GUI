package com.ticket.ticketsys.service;

import com.ticket.ticketsys.pool.TicketPool;
import com.ticket.ticketsys.threads.Customer;
import com.ticket.ticketsys.threads.Vendor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationService {
    private final TicketPool ticketPool;

    public SimulationService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public void startSimulation(int vendors, int customers, int releaseInterval, int retrievalInterval) {
        List<Thread> vendorThreads = new ArrayList<>();
        for (int i = 1; i <= vendors; i++) {
            vendorThreads.add(new Thread(new Vendor(ticketPool, releaseInterval, i)));
        }

        List<Thread> customerThreads = new ArrayList<>();
        for (int i = 1; i <= customers; i++) {
            customerThreads.add(new Thread(new Customer(ticketPool, retrievalInterval, i)));
        }

        vendorThreads.forEach(Thread::start);
        customerThreads.forEach(Thread::start);
    }
}
