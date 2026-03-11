package jua.sergi.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import jua.sergi.exception.OllamaException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Default HTTP client implementation using HttpClient.
 */
public class JavaHttpClient implements HttpClient {

    private final java.net.http.HttpClient client;
    private final ObjectMapper mapper;

    public JavaHttpClient() {
        this.client = java.net.http.HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    @Override
    public <T> T post(String url, Object body, Class<T> responseType) {

        try {

            String json = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new OllamaException("HTTP error: " + response.statusCode());
            }

            return mapper.readValue(response.body(), responseType);

        } catch (IOException | InterruptedException e) {
            throw new OllamaException("Failed request", e);
        }

    }
}
