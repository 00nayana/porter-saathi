package com.porter.saathi.app.client;

import com.jayway.jsonpath.JsonPath;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GPTClient {

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    public String getReply(String userQuery, String context, String lang) throws Exception {

        String instruction = switch (lang) {
            case "hindi" -> "Reply in Hindi";
            case "kannada" -> "Reply in Kannada";
            default -> "Reply in English";
        };

        JSONArray messages = new JSONArray()
                .put(new JSONObject()
                        .put("role", "system")
                        .put("content", "You are Porter Saathi, a voice assistant for drivers. Use the provided driver data for accurate answers. Reply in short, simple " + instruction + "."))
                .put(new JSONObject()
                        .put("role", "system")
                        .put("content", "Driver data: " + context))
                .put(new JSONObject()
                        .put("role", "user")
                        .put("content", userQuery));

        // Build body
        JSONObject body = new JSONObject()
                .put("model", "gpt-4o-mini")
                .put("messages", messages);

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(body.toString(), JSON);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();
        Response response = client.newCall(request).execute();
        return JsonPath.read(response.body().string(), "$.choices[0].message.content");
    }
}
