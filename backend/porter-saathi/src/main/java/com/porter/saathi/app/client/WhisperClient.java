package com.porter.saathi.app.client;

import com.jayway.jsonpath.JsonPath;
import com.porter.saathi.app.models.TranscriptionResult;
import com.porter.saathi.app.util.MultipartHelper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;


@Service
public class WhisperClient {

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    public TranscriptionResult transcribe(MultipartFile audioFile) throws Exception {

        MediaType mediaType = MediaType.parse("text/plain");


        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file",
                        audioFile.getOriginalFilename(),
                        RequestBody.create(audioFile.getBytes(), mediaType))
                .addFormDataPart("model", "whisper-1")
                .addFormDataPart("response_format","verbose_json")
                .build();
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/audio/transcriptions")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();
        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();

        String language = JsonPath.read(responseBody, "$.language");
        String text = JsonPath.read(responseBody, "$.text");


        return new TranscriptionResult(text, language);
    }
}
