package com.porter.saathi.app.service.impl;
import com.porter.saathi.app.client.GPTClient;
import com.porter.saathi.app.client.GoogleTTSClient;
import com.porter.saathi.app.client.GoogleTranslateClient;
import com.porter.saathi.app.client.WhisperClient;
import com.porter.saathi.app.models.*;
import com.porter.saathi.app.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private GoogleTranslateClient googleTranslateClient;

    public byte[] processQuery(MultipartFile audioFile,
                               String driverId,
                               String languageOverride) throws Exception {

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
                Earnings in ₹: %s . It has all the daily / weekly / monthly details. Please parse and answer accordingly.
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

    public byte[] translateAndConvertToSpeech(String text, String language) throws Exception {
        String translatedText = text;
        if(!language.equals("english")) {
            translatedText = googleTranslateClient.translateText(text, "english", language);
        }

        return toSpeech(translatedText, language);
    }

    public byte[] toSpeech(String errorMessage, String language) throws Exception {
        return ttsClient.speak(errorMessage, language);
    }

    public byte[] toDesiredLanguage(MultipartFile audioFile,
                                    String language) throws Exception {


        TranscriptionResult transcriptionResult = whisperClient.transcribe(audioFile);

        String lang = switch (transcriptionResult.getLanguage()) {
            case "hindi" -> "hindi";
            case "english" -> "english";
            default -> "kannada";
        };

        String text = googleTranslateClient.translateText(transcriptionResult.getText(), lang, language);
        return ttsClient.speak(text, language);
    }


    public byte[] statisticsSummary(Map<String, List<EarningsEntry>> jsonData, String language) throws Exception {
        String prompt = """
            Analyze the following driver earnings data and provide a summary add rupee after of price amounts, don't include special chars:
            - Total earnings today
            - Total earnings this week
            - Average trips per day
            - Highlight the day with maximum earnings
            
            Data:
            %s
            """.formatted(jsonData.toString());

            String aiReply = gptClient.getReply("Please provide statistics summary", prompt, language);

            return toSpeech(aiReply, language);
    }

    public byte[] answerFAQQuery(MultipartFile audioFile,
                                 String languageOverride) throws Exception {

        TranscriptionResult transcriptionResult = whisperClient.transcribe(audioFile);
        String userQuery = transcriptionResult.getText();
        String detectedLang = transcriptionResult.getLanguage();

        String finalLang = (detectedLang != null) ? detectedLang : languageOverride;


        String prompt = """
                You are an assistant for delivery drivers.\s
                You must ONLY answer questions based on the documents provided below.\s
                Keep your answers simple, step-by-step, and easy to understand.\s
                If the driver’s question is not covered in the documents, reply:\s
                "I don’t know. Please contact company support."
                
                Here are the documents:
                
                You are an assistant for delivery drivers.\s
                You must ONLY answer questions based on the documents provided below.\s
                Keep your answers simple, step-by-step, and easy to understand.\s
                If the driver’s question is not covered in the documents, reply:\s
                "I don’t know. Please contact company support."
                
                Here are the documents:
                
                1. Help Guide for Delivery Drivers
                - Before work: check license, RC, insurance, fuel, tyres, brakes, phone battery.
                - Start trip: log in to app, check delivery details, click start.
                - Breakdown: park safe, switch on hazard lights, call company at Porter Assistance Number: 1800-123-1234.
                - Accident: help people first, call Ambulance 108 or Police 100, take photos, inform company at 1800-123-1234.
                - Customer complaint: stay calm, click photos, call company support at 1800-123-1234.
                - Keep records: save receipts, photos, FIRs, and upload to app if asked.
                
                2. Vehicle Insurance Help
                - Buying Insurance (New Policy):
                     1. Collect your RC (Registration Certificate) and driving license.
                     2. Go to a trusted insurance website (e.g., www.policybazaar.com or your bank/insurer’s site).
                     3. Enter vehicle details (registration number, year, model).
                     4. Choose type of cover: \s
                        - Third Party (basic, cheaper, mandatory by law). \s
                        - Comprehensive (includes own damage + theft + third party). \s
                     5. Compare prices and benefits.
                     6. Pay online and download soft copy of policy.
                     7. Keep a print copy in the vehicle and upload to DigiLocker digilocker.gov.in.
                   - Renewal: check expiry, compare prices, buy online, save soft copy.
                   - Accident/Theft:
                     - Call your insurer’s toll-free number (e.g., ICICI Lombard: 1800-2666, HDFC Ergo: 1800-2700-700).
                     - File police FIR at nearest station or call 100.
                     - Take photos and keep bills.
                     - Use insurer’s workshop for repair.
                   - Always keep copies of papers.

                3. Contesting a Traffic Challan
                - Go to Government e-challan portal: echallan.parivahan.gov.in/.
                - Enter challan number or vehicle number.
                - Check photo and details.
                - If wrong, upload proof (GPS, photos, receipts).
                - Write why it is wrong.
                - Submit online.
                - If not solved, visit nearest traffic court or call State Traffic Helpline (e.g., Delhi Traffic Helpline 1095).
                
                4. Using DigiLocker
                - Signup: go to DigiLocker app/website: digilocker.gov.in, enter Aadhaar, verify with OTP.
                - Get documents: choose RC/DL/Insurance → fetch from govt records.
                - Upload documents: click upload, select file, add title.
                - Share: create link/QR, send to authority.
                - Keep backup: check sometimes, don’t delete.
                
                Important Numbers (India):
                - Police: 100
                - Ambulance: 108
                - Fire: 101
                - Porter Assistance: 1800-123-1234
                - General Insurance Regulator (IRDAI): 155255
                
                Remember: keep answers short, clear, and step-by-step.
                Don't use any special chars in output so that it is clear
                """;

        String aiReply = gptClient.getReply(userQuery, prompt, finalLang);

        return toSpeech(aiReply, finalLang);
    }
}