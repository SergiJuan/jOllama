package jua.sergi.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
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
 * Default HTTP client using Java 11 HttpClient.
 * Accepts any 2xx status code as success.
 */
public class JavaHttpClient implements HttpClient {

    private final java.net.http.HttpClient client;
    private final ObjectMapper mapper;

    public JavaHttpClient() {
        this.client = java.net.http.HttpClient.newHttpClient();
        this.mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @Override
    public <T> T post(String url, Object body, Class<T> responseType) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            assertSuccess(response.statusCode(), response.body());
            return mapper.readValue(response.body(), responseType);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();           // ← restore flag
            throw new OllamaException("POST request interrupted", e);
        } catch (IOException e) {
            throw new OllamaException("Failed POST request", e);
        }
    }

    @Override
    public <T> void stream(String url, Object body, Class<T> responseType, Consumer<T> onChunk) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            HttpResponse<InputStream> response =
                    client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            assertSuccess(response.statusCode(), null);

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(response.body()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.isBlank()) {
                        onChunk.accept(mapper.readValue(line, responseType));
                    }
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OllamaException("Stream request interrupted", e);
        } catch (IOException e) {
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

            assertSuccess(response.statusCode(), response.body());
            return mapper.readValue(response.body(), responseType);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OllamaException("GET request interrupted", e);
        } catch (IOException e) {
            throw new OllamaException("Failed GET request", e);
        }
    }

    @Override
    public void delete(String url, Object body) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .method("DELETE",
                            HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            assertSuccess(response.statusCode(), response.body());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OllamaException("DELETE request interrupted", e);
        } catch (IOException e) {
            throw new OllamaException("Failed DELETE request", e);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /** Throws OllamaException for any non-2xx status. */
    private void assertSuccess(int statusCode, String body) {
        if (statusCode < 200 || statusCode >= 300) {
            String detail = body != null ? " — " + body : "";
            throw new OllamaException("HTTP error " + statusCode + detail);
        }
    }
}