package com.ticket.ticketsys.threads;

import com.ticket.ticketsys.entity.VendorActivity;
import com.ticket.ticketsys.repository.VendorActivityRepository;
import com.ticket.ticketsys.service.TicketPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Vendor implements Runnable {

    private final TicketPoolService ticketPoolService;
    private final int ticketReleaseRate;
    private final int vendorId;

    @Autowired
    private VendorActivityRepository vendorActivityRepository;

    @Autowired
    public Vendor(TicketPoolService ticketPoolService, int ticketReleaseRate, int vendorId) {
        this.ticketPoolService = ticketPoolService;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (ticketPoolService) {
                if (ticketPoolService.getTotalTicketsRetrieved() >= ticketPoolService.getMaxTicketCapacity()) {
                    ticketPoolService.setVendorFinished();
                    vendorActivityRepository.save(new VendorActivity(null, vendorId, "Stopped Adding - Max Capacity Reached", null));
                    return;
                }

                if (ticketPoolService.getTotalTicketsAdded() < ticketPoolService.getMaxTicketCapacity()) {
                    String ticket = "Ticket-" + (ticketPoolService.getTotalTicketsAdded() + 1);
                    ticketPoolService.addTickets(ticket, vendorId);
                    vendorActivityRepository.save(new VendorActivity(null, vendorId, "Added: " + ticket, null));
                } else {
                    vendorActivityRepository.save(new VendorActivity(null, vendorId, "Stopped Adding - Pool Full", null));
                    ticketPoolService.setVendorFinished();
                    return;
                }
            }

            try {
                Thread.sleep(ticketReleaseRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                vendorActivityRepository.save(new VendorActivity(null, vendorId, "Interrupted", null));
                return;
            }
        }
    }
}
