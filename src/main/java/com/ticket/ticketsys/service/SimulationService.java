package com.ticket.ticketsys.service;

import com.ticket.ticketsys.entity.Configuration;
import com.ticket.ticketsys.threads.Customer;
import com.ticket.ticketsys.threads.Vendor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationService {

    private List<Thread> vendorThreads = new ArrayList<>();
    private List<Thread> customerThreads = new ArrayList<>();
    private boolean isRunning = false;

    public void startSimulation(Configuration configuration) {
        if (isRunning) {
            throw new IllegalStateException("Simulation is already running!");
        }

        isRunning = true;

        // Create vendors
        for (int i = 1; i <= configuration.getNumberOfVendors(); i++) {
            Vendor vendor = new Vendor(new TicketPoolService(configuration.getTotalTickets(), configuration.getMaxTicketCapacity(), configuration.getNumberOfVendors()),
                    configuration.getTicketReleaseRate(), i);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Create customers
        for (int i = 1; i <= configuration.getNumberOfVendors(); i++) {
            Customer customer = new Customer(new TicketPoolService(configuration.getTotalTickets(), configuration.getMaxTicketCapacity(), configuration.getNumberOfVendors()),
                    configuration.getCustomerRetrievalRate(), i);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }
    }

    public void stopSimulation() {
        if (!isRunning) {
            throw new IllegalStateException("Simulation is not running!");
        }

        isRunning = false;

        // Stop all vendor threads
        for (Thread vendorThread : vendorThreads) {
            vendorThread.interrupt();
        }

        // Stop all customer threads
        for (Thread customerThread : customerThreads) {
            customerThread.interrupt();
        }

        vendorThreads.clear();
        customerThreads.clear();
    }
}



