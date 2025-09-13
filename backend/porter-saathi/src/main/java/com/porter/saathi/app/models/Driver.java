package com.porter.saathi.app.models;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    private String driverId;
    private String name;
    private String phone;
    private String preferredLanguage;
}