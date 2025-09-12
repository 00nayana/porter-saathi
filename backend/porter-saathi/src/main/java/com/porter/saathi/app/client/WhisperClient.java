package com.porter.saathi.app.client;

import com.jayway.jsonpath.JsonPath;
import com.porter.saathi.app.models.TranscriptionResult;
import com.porter.saathi.app.util.MultipartHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class WhisperClient {

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    public TranscriptionResult transcribe(MultipartFile audioFile) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.openai.com/v1/audio/transcriptions"))
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("Content-Type", "multipart/form-data")
                .POST(MultipartHelper.ofMultipartData(
                        audioFile,
                        "file",
                        audioFile.getContentType() != null ? audioFile.getContentType() : "audio/mpeg",
                        Map.of("model", "whisper-1", "response_format", "verbose_json")))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String language = JsonPath.read(response.body(), "$.language");
        String text = JsonPath.read(response.body(), "$.text");
        return new TranscriptionResult(language, text);
    }
}
