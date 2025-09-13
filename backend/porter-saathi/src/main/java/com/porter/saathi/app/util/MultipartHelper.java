package com.porter.saathi.app.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.http.HttpRequest;
import java.util.*;

public class MultipartHelper {

    private static final String BOUNDARY = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

    public static HttpRequest.BodyPublisher ofMultipartData(
            MultipartFile file,
            Map<String, String> textParts,
            String boundary
    ) throws IOException {
        var byteArrays = new ArrayList<byte[]>();

        String separator = "--" + boundary + "\r\n";
        String ending = "--" + boundary + "--\r\n";

        // 1️⃣ File part
        StringBuilder filePartHeader = new StringBuilder();
        filePartHeader.append(separator);
        filePartHeader.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                .append(file.getOriginalFilename())
                .append("\"\r\n");
        filePartHeader.append("Content-Type: ")
                .append(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                .append("\r\n\r\n");

        byteArrays.add(filePartHeader.toString().getBytes());
        byteArrays.add(file.getBytes());
        byteArrays.add("\r\n".getBytes());

        // 2️⃣ Text fields
        for (Map.Entry<String, String> entry : textParts.entrySet()) {
            String part = separator +
                    "Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n" +
                    entry.getValue() + "\r\n";
            byteArrays.add(part.getBytes());
        }

        // 3️⃣ Ending boundary
        byteArrays.add(ending.getBytes());

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);

}

public static String getBoundary() {
    return BOUNDARY;
}
}