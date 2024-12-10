package com.ticket.ticketsys.service;

import com.ticket.ticketsys.entity.Configuration;
import com.ticket.ticketsys.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository repository;

    public Configuration saveConfiguration(Configuration config) {
        config.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return repository.save(config);
    }

    public List<Configuration> getAllConfigurations() {
        return repository.findAll();
    }

    public Optional<Configuration> getConfigurationById(String id) {
        return repository.findById(id);
    }
}
