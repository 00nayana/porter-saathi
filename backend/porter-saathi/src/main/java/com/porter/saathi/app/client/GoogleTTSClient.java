package com.porter.saathi.app.client;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class GoogleTTSClient {

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    public byte[] speak(String text, String language) throws Exception {

        String languageCode = switch (language) {
            case "hi" -> "hi-IN";
            case "kn" -> "kn-IN";
            default -> "en-US";
        };

        HttpClient client = HttpClient.newHttpClient();

        String body = """
                {
                  "input": {"text": "%s"},
                  "voice": {"languageCode": "%s"},
                  "audioConfig": {"audioEncoding": "MP3"}
                }
                """.formatted(text, languageCode);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://texttospeech.googleapis.com/v1/text:synthesize?key=" + GOOGLE_API_KEY))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String audioBase64 = JsonPath.read(response.body(), "$.audioContent");

        return Base64.getDecoder().decode(audioBase64);
    }
}
