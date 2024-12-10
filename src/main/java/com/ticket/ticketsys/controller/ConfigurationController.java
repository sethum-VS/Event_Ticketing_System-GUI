package com.ticket.ticketsys.controller;

import com.ticket.ticketsys.entity.Configuration;
import com.ticket.ticketsys.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {

    @Autowired
    private ConfigurationService service;

    @PostMapping
    public ResponseEntity<Configuration> saveConfiguration(@RequestBody Configuration config) {
        return ResponseEntity.ok(service.saveConfiguration(config));
    }

    @GetMapping
    public ResponseEntity<List<Configuration>> getAllConfigurations() {
        return ResponseEntity.ok(service.getAllConfigurations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Configuration> getConfigurationById(@PathVariable String id) {
        return service.getConfigurationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

