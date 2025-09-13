package com.porter.saathi.app.client;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleTranslateClient {

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    private static final OkHttpClient client = new OkHttpClient();

    public String translateText(String text, String sourceLang, String targetLang) throws Exception {

        if(sourceLang.equals(targetLang)) {
            return text;
        }

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Build JSON body safely
        JSONObject bodyJson = new JSONObject()
                .put("q", text)
                .put("source", getLanguage(sourceLang))   // e.g. "hi" for Hindi
                .put("target", getLanguage(targetLang))   // e.g. "en" for English
                .put("format", "text");

        RequestBody body = RequestBody.create(bodyJson.toString(), JSON);

        Request request = new Request.Builder()
                .url("https://translation.googleapis.com/language/translate/v2?key=" + GOOGLE_API_KEY)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code " + response + " - " + response.body().string());
            }

            String jsonResponse = response.body().string();

            // Extract translated text
            JSONObject json = new JSONObject(jsonResponse);
            return json.getJSONObject("data")
                    .getJSONArray("translations")
                    .getJSONObject(0)
                    .getString("translatedText");
        }
    }


    String getLanguage(String language) throws Exception {
        String languageCode = switch (language) {
            case "hindi" -> "hi";
            case "english" -> "en";
            default -> "kn";
        };
        return languageCode;
    }

}
