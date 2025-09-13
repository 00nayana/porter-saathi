package com.porter.saathi.app.service.impl;

import com.porter.saathi.app.models.Driver;
import com.porter.saathi.app.models.Earnings;
import com.porter.saathi.app.models.Penalty;
import com.porter.saathi.app.models.Reward;
import com.porter.saathi.app.service.DriverService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverServiceImpl implements DriverService {
    private static final Map<String, Driver> drivers = new HashMap<>();
    private static final Map<String, List<Earnings>> earnings = new HashMap<>();
    private static final Map<String, List<Penalty>> penalties = new HashMap<>();
    private static final Map<String, List<Reward>> rewards = new HashMap<>();

    static {
        // Mock Driver
        Driver d1 = new Driver("D123", "Ramesh Kumar", "+91-9876543210", "hi-IN");
        drivers.put(d1.getDriverId(), d1);

        // Mock Earnings
        Earnings e1 = new Earnings("D123", LocalDate.now(), 1500, 100, 50, 1350);
        earnings.put(d1.getDriverId(), List.of(e1));

        Earnings e2 = new Earnings("D123", LocalDate.now().minusDays(1), 1200, 80, 40, 1080);
        earnings.put(d1.getDriverId(), List.of(e1));

        // Mock Penalty
        Penalty p1 = new Penalty("P789", "D123", LocalDate.now().minusDays(1),
                "Delivery was late by 30 minutes", 50);
        penalties.put(d1.getDriverId(), List.of(p1));

        // Mock Rewards
        Reward r1 = new Reward("R001", "D123", LocalDate.now().minusDays(2),
                "Completed 10 trips in a day", 200);
        rewards.put(d1.getDriverId(), List.of(r1));
    }

    @Override
    public Driver getDriverProfile(String driverId) {
        return drivers.get(driverId);
    }

    @Override
    public Earnings getTodaysEarnings(String driverId) {
        return earnings.get(driverId).get(0);
    }

    @Override
    public Earnings getYesterdaysEarnings(String driverId) {
        return earnings.get(driverId).get(1);
    }

    @Override
    public Penalty getLatestPenalty(String driverId) {
        return penalties.get(driverId).get(0);
    }

    @Override
    public List<Reward> getRewards(String driverId) {
        return rewards.get(driverId);
    }
}
