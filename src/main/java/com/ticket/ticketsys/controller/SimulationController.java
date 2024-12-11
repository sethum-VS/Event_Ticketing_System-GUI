package com.ticket.ticketsys.controller;

import com.ticket.ticketsys.pool.TicketPool;
import com.ticket.ticketsys.repository.ConfigurationRepository;
import com.ticket.ticketsys.threads.Customer;
import com.ticket.ticketsys.threads.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private ConfigurationRepository repository;

    @PostMapping("/start")
    public String startSimulation(@RequestParam int vendors, @RequestParam int customers, @RequestParam int totalTickets,
                                  @RequestParam int releaseRate, @RequestParam int retrievalRate,
                                  @RequestParam int maxCapacity) {

        ticketPool.initialize(totalTickets, maxCapacity, vendors);

        List<Thread> vendorThreads = new ArrayList<>();
        for (int i = 1; i <= vendors; i++) {
            vendorThreads.add(new Thread(new Vendor(ticketPool, releaseRate, i)));
        }

        List<Thread> customerThreads = new ArrayList<>();
        for (int i = 1; i <= customers; i++) {
            customerThreads.add(new Thread(new Customer(ticketPool, retrievalRate, customers)));
        }

        vendorThreads.forEach(Thread::start);
        customerThreads.forEach(Thread::start);

        return "Simulation started with " + vendors + " vendors and " + customers + " customers.";
    }
}
