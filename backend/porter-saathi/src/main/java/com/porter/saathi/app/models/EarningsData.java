package com.porter.saathi.app.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EarningsData {
    private List<EarningsEntry> today;
    private List<EarningsEntry> yesterday;
    private List<EarningsEntry> thisWeek;
    private List<EarningsEntry> lastWeek;
    private List<EarningsEntry> thisMonth;
    private List<EarningsEntry> lastMonth;
}
