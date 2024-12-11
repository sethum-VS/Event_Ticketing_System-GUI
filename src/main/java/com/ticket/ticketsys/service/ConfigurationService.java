package com.ticket.ticketsys.service;

import com.ticket.ticketsys.entity.Configuration;
import com.ticket.ticketsys.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    public Configuration saveConfiguration(int totalTickets, int maxTicketCapacity, int ticketReleaseRate, int customerRetrievalRate) {
        Configuration configuration = new Configuration(
                null,
                totalTickets,
                maxTicketCapacity,
                ticketReleaseRate,
                customerRetrievalRate,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        return configurationRepository.save(configuration);
    }

    public List<Configuration> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    public Configuration getConfigurationById(String id) {
        return configurationRepository.findById(id).orElse(null);
    }
}
