package com.porter.saathi.app.service.impl;
import com.porter.saathi.app.client.GPTClient;
import com.porter.saathi.app.client.GoogleTTSClient;
import com.porter.saathi.app.client.WhisperClient;
import com.porter.saathi.app.models.*;
import com.porter.saathi.app.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AssistantService {

    @Autowired
    private DriverService driverService;

    @Autowired
    private WhisperClient whisperClient;

    @Autowired
    private GPTClient gptClient;

    @Autowired
    private GoogleTTSClient ttsClient;

    public byte[] processQuery(MultipartFile audioFile, String driverId, String languageOverride) throws Exception {

        // Step 1: Speech → Text
        TranscriptionResult transcriptionResult = whisperClient.transcribe(audioFile);
        String userQuery = transcriptionResult.getText();
        String detectedLang = transcriptionResult.getLanguage();

        String finalLang = (languageOverride != null) ? languageOverride : detectedLang;

        // Step 2: Fetch driver context
        Driver driver = driverService.getDriverProfile(driverId);
        Earnings earnings = driverService.getTodaysEarnings(driverId);
        Penalty penalty = driverService.getLatestPenalty(driverId);
        List<Reward> rewards = driverService.getRewards(driverId);

        String context = """
                Driver Name: %s
                Today's Earnings: ₹%d
                Expenses: ₹%d
                Penalties: ₹%d (Latest Reason: %s)
                Net Earnings: ₹%d
                Recent Rewards: %s
                """.formatted(
                driver.getName(),
                earnings.getTotalEarnings(),
                earnings.getExpenses(),
                earnings.getPenalties(),
                penalty.getReason(),
                earnings.getNetEarnings(),
                rewards.stream().map(r -> r.getReason() + " (₹" + r.getAmount() + ")").toList()
        );

        // Step 2: Text → GPT reply
        String aiReply = gptClient.getReply(userQuery, context, finalLang);

        // Step 3: GPT reply → TTS audio
        return toSpeech(aiReply, finalLang);
    }

    public byte[] toSpeech(String errorMessage, String language) throws Exception {
        return ttsClient.speak(errorMessage, language);
    }
}