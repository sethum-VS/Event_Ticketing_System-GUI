package com.ticket.ticketsys.service;

import com.ticket.ticketsys.entity.CustomerActivity;
import com.ticket.ticketsys.entity.VendorActivity;
import com.ticket.ticketsys.repository.CustomerActivityRepository;
import com.ticket.ticketsys.repository.VendorActivityRepository;
import com.ticket.ticketsys.threads.Customer;
import com.ticket.ticketsys.threads.Vendor;
import com.ticket.ticketsys.service.TicketPoolService;
import com.ticket.ticketsys.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class SimulationService {

    private static final int NUM_VENDORS = 2;
    private static final int NUM_CUSTOMERS = 2;
    private final LoggerService loggerService;

    @Autowired
    private TicketPoolService ticketPoolService;

    @Autowired
    private CustomerActivityRepository customerActivityRepository;

    @Autowired
    private VendorActivityRepository vendorActivityRepository;

    private Thread[] vendorThreads = new Thread[NUM_VENDORS];
    private Thread[] customerThreads = new Thread[NUM_CUSTOMERS];

    private volatile boolean isRunning = false;

    public SimulationService(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public void startSimulation(int totalTickets, int maxTicketCapacity, int ticketReleaseRate, int customerRetrievalRate) {
        ticketPoolService.initializePool(totalTickets, maxTicketCapacity);

        // Create and start static vendor threads
        Thread vendor1 = new Thread(new Vendor(1, ticketReleaseRate, ticketPoolService , loggerService));
        Thread vendor2 = new Thread(new Vendor(2, ticketReleaseRate, ticketPoolService, loggerService));

        // Create and start static customer threads
        Thread customer1 = new Thread(new Customer(1, customerRetrievalRate, ticketPoolService, loggerService));
        Thread customer2 = new Thread(new Customer(2, customerRetrievalRate, ticketPoolService, loggerService));

        vendor1.start();
        vendor2.start();
        customer1.start();
        customer2.start();

        try {
            vendor1.join();
            vendor2.join();
            customer1.join();
            customer2.join();
        } catch (InterruptedException e) {
            loggerService.logInfo("Simulation interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        loggerService.logInfo("Simulation completed. Resetting the system.");
        ticketPoolService.resetPool();
    }


    public void resetSimulation() {
        if (!isRunning) {
            throw new IllegalStateException("No active simulation to reset.");
        }

        // Stop all threads
        for (Thread vendorThread : vendorThreads) {
            if (vendorThread != null) {
                vendorThread.interrupt();
            }
        }
        for (Thread customerThread : customerThreads) {
            if (customerThread != null) {
                customerThread.interrupt();
            }
        }

        // Reset the ticket pool
        ticketPoolService.resetPool();
        isRunning = false;
    }


    public boolean isSimulationRunning() {
        return isRunning;
    }
}
