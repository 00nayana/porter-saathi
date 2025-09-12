package com.porter.saathi.app.service;

import com.porter.saathi.app.models.Driver;
import com.porter.saathi.app.models.Earnings;
import com.porter.saathi.app.models.Penalty;
import com.porter.saathi.app.models.Reward;

import java.util.List;

public interface DriverService {

    Driver getDriverProfile(String driverId);

    Earnings getTodaysEarnings(String driverId);

    Penalty getLatestPenalty(String driverId);

    List<Reward> getRewards(String driverId);
}
