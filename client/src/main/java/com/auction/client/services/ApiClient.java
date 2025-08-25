package com.auction.client.services;

import java.io.File;
import java.net.http.*;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080";

    private static final HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> post(String path, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> postMultipart(String path, Map<Object, Object> data, List<File> images) throws Exception {
        String boundary = "Boundary-" + ThreadLocalRandom.current().nextInt(100000, 999999);
        var byteArrays = new ArrayList<byte[]>();

        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(("--" + boundary + "\r\n").getBytes());
            byteArrays.add(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes());
            byteArrays.add((entry.getValue().toString() + "\r\n").getBytes());
        }

        if (images != null) {
            for (File image : images) {
                String filename = image.getName();
                String mimeType = Files.probeContentType(image.toPath());
                if (mimeType == null) mimeType = "application/octet-stream";

                byteArrays.add(("--" + boundary + "\r\n").getBytes());
                byteArrays.add(("Content-Disposition: form-data; name=\"images\"; filename=\"" + filename + "\"\r\n").getBytes());
                byteArrays.add(("Content-Type: " + mimeType + "\r\n\r\n").getBytes());
                byteArrays.add(Files.readAllBytes(image.toPath()));
                byteArrays.add("\r\n".getBytes());
            }
        }

        byteArrays.add(("--" + boundary + "--\r\n").getBytes());

        var request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArrays(byteArrays))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
