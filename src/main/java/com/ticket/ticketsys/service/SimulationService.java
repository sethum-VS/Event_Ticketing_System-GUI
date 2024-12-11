package com.ticket.ticketsys.service;

import com.ticket.ticketsys.entity.CustomerActivity;
import com.ticket.ticketsys.entity.VendorActivity;
import com.ticket.ticketsys.repository.CustomerActivityRepository;
import com.ticket.ticketsys.repository.VendorActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SimulationService {

    private static final int NUM_VENDORS = 2;
    private static final int NUM_CUSTOMERS = 2;

    @Autowired
    private TicketPoolService ticketPoolService;

    @Autowired
    private CustomerActivityRepository customerActivityRepository;

    @Autowired
    private VendorActivityRepository vendorActivityRepository;

    private Thread[] vendorThreads = new Thread[NUM_VENDORS];
    private Thread[] customerThreads = new Thread[NUM_CUSTOMERS];

    private volatile boolean isRunning = false;

    public void startSimulation(int totalTickets, int maxTicketCapacity, int ticketReleaseRate, int customerRetrievalRate) {
        if (isRunning) {
            throw new IllegalStateException("Simulation is already running!");
        }

        // Initialize ticket pool
        ticketPoolService.initializePool(totalTickets, maxTicketCapacity);

        // Start vendor threads
        for (int i = 0; i < NUM_VENDORS; i++) {
            int vendorId = i + 1;
            vendorThreads[i] = new Thread(() -> runVendor(vendorId, ticketReleaseRate));
            vendorThreads[i].start();
        }

        // Start customer threads
        for (int i = 0; i < NUM_CUSTOMERS; i++) {
            int customerId = i + 1;
            customerThreads[i] = new Thread(() -> runCustomer(customerId, customerRetrievalRate));
            customerThreads[i].start();
        }

        isRunning = true;
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

    private void runVendor(int vendorId, int releaseRate) {
        try {
            while (ticketPoolService.canAddTickets()) {
                String ticketId = "Ticket-" + System.nanoTime();
                ticketPoolService.addTicket(ticketId, vendorId);

                // Log the action in the database
                VendorActivity activity = new VendorActivity(null, vendorId, ticketId, getCurrentTimestamp());
                vendorActivityRepository.save(activity);

                Thread.sleep(releaseRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void runCustomer(int customerId, int retrievalRate) {
        try {
            while (ticketPoolService.canRetrieveTickets()) {
                String ticket = ticketPoolService.removeTicket(customerId);

                if (ticket != null) {
                    // Log the action in the database
                    CustomerActivity activity = new CustomerActivity(null, customerId, ticket, getCurrentTimestamp());
                    customerActivityRepository.save(activity);
                }

                Thread.sleep(retrievalRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public boolean isSimulationRunning() {
        return isRunning;
    }
}
