package com.ticket.ticketsys.controller;

import com.ticket.ticketsys.entity.CustomerActivity;
import com.ticket.ticketsys.entity.LogEntry;
import com.ticket.ticketsys.entity.VendorActivity;
import com.ticket.ticketsys.service.SimulationService;
import com.ticket.ticketsys.repository.CustomerActivityRepository;
import com.ticket.ticketsys.repository.VendorActivityRepository;
import com.ticket.ticketsys.dto.ConfigurationRequest; // Import ConfigurationRequest instead of SimulationRequest
import com.ticket.ticketsys.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private CustomerActivityRepository customerActivityRepository;

    @Autowired
    private VendorActivityRepository vendorActivityRepository;

    /**
     * Start a simulation with user-provided parameters.
     *
     * @param request Contains simulation parameters from the frontend.
     * @return Confirmation message.
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startSimulation(@RequestBody ConfigurationRequest request) {
        try {
            simulationService.startSimulation(
                    request.getTotalTickets(),
                    request.getMaxTicketCapacity(),
                    request.getTicketReleaseRate(),
                    request.getCustomerRetrievalRate()
            );
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Simulation started successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Reset the simulation after termination.
     *
     * @return Confirmation message.
     */
    @PostMapping("/reset")
    public String resetSimulation() {
        try {
            simulationService.resetSimulation();
            return "Simulation reset successfully.";
        } catch (IllegalStateException e) {
            return "No active simulation to reset: " + e.getMessage();
        }
    }

    /**
     * Get the current state of the simulation.
     *
     * @return Simulation running status.
     */
    @GetMapping("/state")
    public boolean getSimulationState() {
        return simulationService.isSimulationRunning();
    }

    /**
     * Get all customer activity logs.
     *
     * @return List of customer activities.
     */
    @GetMapping("/customers/activities")
    public List<CustomerActivity> getCustomerActivities() {
        return customerActivityRepository.findAll();
    }

    /**
     * Get all vendor activity logs.
     *
     * @return List of vendor activities.
     */
    @GetMapping("/vendors/activities")
    public List<VendorActivity> getVendorActivities() {
        return vendorActivityRepository.findAll();
    }

    // Fetch logs
    @GetMapping("/logs")
    public ResponseEntity<List<LogEntry>> getLogs() {
        return ResponseEntity.ok(loggerService.getAllLogs());
    }
}
