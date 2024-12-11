package com.ticket.ticketsys.controller;


import com.ticket.ticketsys.entity.Configuration;
import com.ticket.ticketsys.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {

    @Autowired
    private ConfigurationService service;

    @PostMapping
    public Configuration saveConfiguration(@RequestBody Configuration config) {
        return service.saveConfiguration(config);
    }

    @GetMapping
    public List<Configuration> getAllConfigurations() {
        return service.getAllConfigurations();
    }

    @GetMapping("/{id}")
    public Configuration getConfiguration(@PathVariable String id) {
        return service.getConfigurationById(id);
    }
}


