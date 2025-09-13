package com.porter.saathi.app.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TranscriptionResult {
    private String text;
    private String language;
}
