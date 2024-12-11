package com.ticket.ticketsys.controller;
import com.ticket.ticketsys.dto.ConfigurationRequest;


import com.ticket.ticketsys.entity.Configuration;
import com.ticket.ticketsys.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @PostMapping
    public Configuration saveConfiguration(@RequestBody ConfigurationRequest request) {
        return configurationService.saveConfiguration(
                request.getTotalTickets(),
                request.getMaxTicketCapacity(),
                request.getTicketReleaseRate(),
                request.getCustomerRetrievalRate()
        );
    }

    @GetMapping
    public List<Configuration> getAllConfigurations() {
        return configurationService.getAllConfigurations();
    }

    @GetMapping("/{id}")
    public Configuration getConfigurationById(@PathVariable String id) {
        return configurationService.getConfigurationById(id);
    }
}
