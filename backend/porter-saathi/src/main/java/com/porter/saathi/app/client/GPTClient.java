package com.porter.saathi.app.client;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GPTClient {

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    public String getReply(String userQuery, String context, String lang) throws Exception {

        String instruction = switch (lang) {
            case "hi" -> "Reply in Hindi";
            case "kn" -> "Reply in Kannada";
            default -> "Reply in English";
        };

        HttpClient client = HttpClient.newHttpClient();

        String body = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {"role": "system", "content": "You are Porter Saathi, a voice assistant for drivers. Use the provided driver data for accurate answers. Reply in short, simple %s."},
            {"role": "system", "content": "Driver data: %s"},
            {"role": "user", "content": "%s"}
          ]
        }
        """.formatted(instruction, context, userQuery);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return JsonPath.read(response.body(), "$.choices[0].message.content");
    }
}
