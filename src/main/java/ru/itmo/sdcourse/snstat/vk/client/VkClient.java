package ru.itmo.sdcourse.snstat.vk.client;

import ru.itmo.sdcourse.snstat.vk.exception.VkException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class VkClient {
    private static final String API_VERSION = "5.131";

    private final boolean secure;
    private final String host;
    private final int port;
    private final String method;
    private final String accessToken;

    public VkClient(boolean secure, String host, int port, String method, String accessToken) {
        this.secure = secure;
        this.host = host;
        this.port = port;
        this.method = method;
        this.accessToken = accessToken;
    }

    public String fetch(Map<String, String> params) {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpRequest request = HttpRequest.newBuilder(getUri(params)).GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        if (response.statusCode() != HttpURLConnection.HTTP_OK)
            throw new VkException("Vk returned not ok status code: " + response.statusCode());

        return response.body();
    }

    private URI getUri(Map<String, String> params) {
        StringBuilder uriBuilder = new StringBuilder()
                .append(secure ? "https" : "http").append("://")
                .append(encode(host)).append(":").append(port)
                .append("/method/").append(method)
                .append("?")
                .append("access_token=").append(accessToken)
                .append("&v=").append(API_VERSION);
        params.forEach((key, value) -> uriBuilder.append("&").append(encode(key)).append("=").append(encode(value)));

        try {
            return new URI(uriBuilder.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String encode(String toEncode) {
        return URLEncoder.encode(toEncode, StandardCharsets.UTF_8);
    }
}
