package com.porter.saathi.app.client;

import com.jayway.jsonpath.JsonPath;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Service
public class GoogleTTSClient {

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    public byte[] speak(String text, String language) throws Exception {

        String languageCode = switch (language) {
            case "hindi" -> "hi-IN";
            case "kannada" -> "kn-IN";
            default -> "en-US";
        };

        String body = new JSONObject()
                .put("input", new JSONObject().put("text", text))
                .put("voice", new JSONObject().put("languageCode", languageCode))
                .put("audioConfig", new JSONObject().put("audioEncoding", "MP3"))
                .toString();


        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://texttospeech.googleapis.com/v1/text:synthesize?key=" + GOOGLE_API_KEY)
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String audioBase64 = JsonPath.read(response.body().string(), "$.audioContent");

        return Base64.getDecoder().decode(audioBase64);
    }
}
