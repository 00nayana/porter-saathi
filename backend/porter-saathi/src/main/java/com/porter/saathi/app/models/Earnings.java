package com.porter.saathi.app.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Earnings {
    private String driverId;
    private LocalDate date;
    private int totalEarnings;
    private int expenses;
    private int penalties;
    private int netEarnings;
}
