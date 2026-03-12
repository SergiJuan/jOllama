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
 * Supports both standard blocking requests and NDJSON streaming.
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

    /**
     * Sends a streaming POST request and delivers each NDJSON line
     * as a parsed object to the provided consumer.
     *
     * <p>Ollama streams responses as newline-delimited JSON (NDJSON),
     * where each line is a complete JSON object. This method reads
     * the response line by line and deserializes each chunk.</p>
     *
     * @param url          endpoint URL
     * @param body         request body (will be serialized to JSON)
     * @param responseType class to deserialize each chunk into
     * @param onChunk      consumer called for each received chunk
     * @param <T>          response chunk type
     */
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
}