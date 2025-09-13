package com.porter.saathi.app.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EarningsEntry {

    private String timeframe;
    private int earnings;
    private int trips;
}
