package com.porter.saathi.app.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.util.*;

public class MultipartHelper {

    private static final String BOUNDARY = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

    public static HttpRequest.BodyPublisher ofMultipartData(
            MultipartFile multipartFile,
            String fieldName,
            String mimeType,
            Map<Object, Object> data
    ) throws IOException {
        var byteArrays = new ArrayList<byte[]>();

        // Add text fields
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(("--" + BOUNDARY + "\r\n").getBytes());
            byteArrays.add(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n")
                    .getBytes());
            byteArrays.add(entry.getValue().toString().getBytes());
            byteArrays.add("\r\n".getBytes());
        }

        // Add file field
        byteArrays.add(("--" + BOUNDARY + "\r\n").getBytes());
        byteArrays.add(("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + multipartFile.getName() + "\"\r\n")
                .getBytes());
        byteArrays.add(("Content-Type: " + mimeType + "\r\n\r\n").getBytes());
        byteArrays.add(multipartFile.getBytes());
        byteArrays.add("\r\n".getBytes());

        // End boundary
        byteArrays.add(("--" + BOUNDARY + "--\r\n").getBytes());

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

    public static String getBoundary() {
        return BOUNDARY;
    }
}