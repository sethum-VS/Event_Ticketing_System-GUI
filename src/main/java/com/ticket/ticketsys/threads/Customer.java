package com.ticket.ticketsys.threads;

import com.ticket.ticketsys.entity.CustomerActivity;
import com.ticket.ticketsys.repository.CustomerActivityRepository;
import com.ticket.ticketsys.service.TicketPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Customer implements Runnable {

    private final TicketPoolService ticketPoolService;
    private final int customerRetrievalInterval;
    private final int customerId;

    @Autowired
    private CustomerActivityRepository customerActivityRepository;

    @Autowired
    public Customer(TicketPoolService ticketPoolService, int customerRetrievalRate, int customerId) {
        this.ticketPoolService = ticketPoolService;
        this.customerRetrievalRate = customerRetrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        while (true) {
            String ticket = ticketPoolService.removeTicket(customerId);

            if (ticket != null) {
                customerActivityRepository.save(new CustomerActivity(null, customerId, ticket, null));
            } else if (ticketPoolService.isComplete()) {
                customerActivityRepository.save(new CustomerActivity(null, customerId, "Finished - No More Tickets", null));
                break;
            }

            try {
                Thread.sleep(customerRetrievalInterval); // Simulate retrieval interval
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                customerActivityRepository.save(new CustomerActivity(null, customerId, "Interrupted", null));
                return;
            }
        }
    }
}


