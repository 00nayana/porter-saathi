package com.porter.saathi.app.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Penalty {
    private String penaltyId;
    private String driverId;
    private LocalDate date;
    private String reason;
    private int amount;
}
