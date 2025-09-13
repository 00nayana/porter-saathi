package com.porter.saathi.app.service.impl;

import com.porter.saathi.app.models.EarningsEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverEarningsService {

    @Autowired
    private AssistantService assistantService;

    private final Map<String, Map<String, List<EarningsEntry>>> driverEarningsEntryMap = new HashMap<>();

    DriverEarningsService() {
        Map<String, List<EarningsEntry>> earningsMap = new HashMap<>();

        // TODAY
        earningsMap.put("today", Arrays.asList(
                new EarningsEntry("6–8 AM", 80, 1),
                new EarningsEntry("8–10 AM", 150, 2),
                new EarningsEntry("10–12 PM", 100, 1),
                new EarningsEntry("12–2 PM", 180, 2),
                new EarningsEntry("2–4 PM", 220, 2)
        ));

        // YESTERDAY
        earningsMap.put("yesterday", Arrays.asList(
                new EarningsEntry("6–10 AM", 400, 3),
                new EarningsEntry("10–2 PM", 700, 5),
                new EarningsEntry("2–6 PM", 500, 4),
                new EarningsEntry("6–10 PM", 350, 2)
        ));

        // THIS WEEK
        earningsMap.put("thisWeek", Arrays.asList(
                new EarningsEntry("Mon (8 Sep)", 2000, 15),
                new EarningsEntry("Tue (9 Sep)", 1800, 12),
                new EarningsEntry("Wed (10 Sep)", 2200, 16),
                new EarningsEntry("Thu (11 Sep)", 2500, 18),
                new EarningsEntry("Fri (12 Sep)", 3000, 20),
                new EarningsEntry("Sat (13 Sep)", 730, 8)
        ));

        // LAST WEEK
        earningsMap.put("lastWeek", Arrays.asList(
                new EarningsEntry("Mon (1 Sep)", 1500, 10),
                new EarningsEntry("Tue (2 Sep)", 1700, 13),
                new EarningsEntry("Wed (3 Sep)", 2100, 15),
                new EarningsEntry("Thu (4 Sep)", 2000, 14),
                new EarningsEntry("Fri (5 Sep)", 2300, 16),
                new EarningsEntry("Sat (6 Sep)", 2200, 12),
                new EarningsEntry("Sun (7 Sep)", 1800, 11)
        ));

        // THIS MONTH
        earningsMap.put("thisMonth", Arrays.asList(
                new EarningsEntry("Week 1 (1–7 Sep)", 8000, 58),
                new EarningsEntry("Week 2 (8–13 Sep)", 9230, 73)
        ));

        // LAST MONTH
        earningsMap.put("lastMonth", Arrays.asList(
                new EarningsEntry("Week 1 (1–7 Aug)", 7000, 52),
                new EarningsEntry("Week 2 (8–14 Aug)", 8500, 68),
                new EarningsEntry("Week 3 (15–21 Aug)", 9000, 63),
                new EarningsEntry("Week 4 (22–28 Aug)", 10000, 77)
        ));

        // Add driver
        driverEarningsEntryMap.put("D123", earningsMap);

    }

    public Map<String, List<EarningsEntry>> getDriverEarnings(String driverId) {
        return driverEarningsEntryMap.get(driverId);
    }

    public byte[] getDriverEarningsSummary(String driverId, String language) throws Exception {
        return assistantService.statisticsSummary(getDriverEarnings(driverId), language);
    }
}
