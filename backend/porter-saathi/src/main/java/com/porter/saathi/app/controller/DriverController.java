package com.porter.saathi.app.controller;

import com.porter.saathi.app.models.Driver;
import com.porter.saathi.app.models.Earnings;
import com.porter.saathi.app.models.Penalty;
import com.porter.saathi.app.models.Reward;
import com.porter.saathi.app.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @GetMapping("/{driverId}/profile")
    public Driver getProfile(@PathVariable String driverId) {
        return driverService.getDriverProfile(driverId);
    }

    @GetMapping("/{driverId}/earnings/today")
    public Earnings getTodaysEarnings(@PathVariable String driverId) {
        return driverService.getTodaysEarnings(driverId);
    }

    @GetMapping("/{driverId}/penalties/latest")
    public Penalty getLatestPenalty(@PathVariable String driverId) {
        return driverService.getLatestPenalty(driverId);
    }

    @GetMapping("/{driverId}/rewards")
    public List<Reward> getRewards(@PathVariable String driverId) {
        return driverService.getRewards(driverId);
    }
}
