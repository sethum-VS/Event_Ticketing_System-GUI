package com.ticket.ticketsys.controller;


import com.ticket.ticketsys.entity.Configuration;
import com.ticket.ticketsys.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    @Autowired
    private SimulationService simulationService;

    @PostMapping("/start")
    public String startSimulation(@RequestBody Configuration configuration) {
        simulationService.startSimulation(configuration);
        return "Simulation started successfully!";
    }

    @PostMapping("/stop")
    public String stopSimulation() {
        simulationService.stopSimulation();
        return "Simulation stopped successfully!";
    }
}


