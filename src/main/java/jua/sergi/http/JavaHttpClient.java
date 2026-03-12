package jua.sergi.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import jua.sergi.exception.OllamaException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

/**
 * Default HTTP client implementation using Java 11 HttpClient.
 * Supports GET, POST, DELETE and NDJSON streaming.
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
            throw new OllamaException("Failed POST request", e);
        }
    }

    @Override
    public <T> void stream(String url, Object body, Class<T> responseType, Consumer<T> onChunk) {
        try {
            String json = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<InputStream> response =
                    client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                throw new OllamaException("HTTP error: " + response.statusCode());
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.isBlank()) {
                        T chunk = mapper.readValue(line, responseType);
                        onChunk.accept(chunk);
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            throw new OllamaException("Failed streaming request", e);
        }
    }

    @Override
    public <T> T get(String url, Class<T> responseType) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new OllamaException("HTTP error: " + response.statusCode());
            }

            return mapper.readValue(response.body(), responseType);

        } catch (IOException | InterruptedException e) {
            throw new OllamaException("Failed GET request", e);
        }
    }

    @Override
    public void delete(String url, Object body) {
        try {
            String json = mapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new OllamaException("HTTP error: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            throw new OllamaException("Failed DELETE request", e);
        }
    }
}