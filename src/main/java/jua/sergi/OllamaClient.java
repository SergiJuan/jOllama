package jua.sergi;

import jua.sergi.http.HttpClient;
import jua.sergi.http.JavaHttpClient;
import jua.sergi.model.Options;
import jua.sergi.model.request.ChatRequest;
import jua.sergi.model.request.EmbeddingRequest;
import jua.sergi.model.request.GenerateRequest;
import jua.sergi.model.response.ChatResponse;
import jua.sergi.model.response.EmbeddingResponse;
import jua.sergi.model.response.GenerateResponse;

import java.util.function.Consumer;

/**
 * Main client used to communicate with the Ollama API.
 *
 * <pre>{@code
 * OllamaClient client = OllamaClient.builder()
 *         .host("http://localhost:11434")
 *         .build();
 *
 * Options opts = Options.builder().temperature(0.5).seed(42).build();
 * GenerateResponse r = client.generate("llama3", "Hello!", opts);
 * }</pre>
 */
public class OllamaClient {

    private final String host;
    private final HttpClient httpClient;

    private OllamaClient(Builder builder) {
        this.host       = builder.host;
        this.httpClient = builder.httpClient;
    }

    // -------------------------------------------------------------------------
    // Generate
    // -------------------------------------------------------------------------

    /** Blocking generation with default options. */
    public GenerateResponse generate(String model, String prompt) {
        return generate(model, prompt, null, null);
    }

    /** Blocking generation with custom options. */
    public GenerateResponse generate(String model, String prompt, Options options) {
        return generate(model, prompt, null, options);
    }

    /** Blocking generation with system prompt and custom options. */
    public GenerateResponse generate(String model, String prompt, String system, Options options) {
        GenerateRequest request = new GenerateRequest(model, prompt, false);
        request.setSystem(system);
        request.setOptions(options);
        return httpClient.post(host + "/api/generate", request, GenerateResponse.class);
    }

    /** Streaming generation with default options. */
    public void generateStreaming(String model, String prompt, Consumer<GenerateResponse> onChunk) {
        generateStreaming(model, prompt, null, null, onChunk);
    }

    /** Streaming generation with custom options. */
    public void generateStreaming(String model, String prompt, Options options,
                                  Consumer<GenerateResponse> onChunk) {
        generateStreaming(model, prompt, null, options, onChunk);
    }

    /** Streaming generation with system prompt and custom options. */
    public void generateStreaming(String model, String prompt, String system,
                                  Options options, Consumer<GenerateResponse> onChunk) {
        GenerateRequest request = new GenerateRequest(model, prompt, true);
        request.setSystem(system);
        request.setOptions(options);
        httpClient.stream(host + "/api/generate", request, GenerateResponse.class, onChunk);
    }

    // -------------------------------------------------------------------------
    // Chat
    // -------------------------------------------------------------------------

    /** Blocking chat. */
    public ChatResponse chat(ChatRequest request) {
        return httpClient.post(host + "/api/chat", request, ChatResponse.class);
    }

    /** Streaming chat. */
    public void chatStreaming(ChatRequest request, Consumer<ChatResponse> onChunk) {
        request.setStream(true);
        httpClient.stream(host + "/api/chat", request, ChatResponse.class, onChunk);
    }

    // -------------------------------------------------------------------------
    // Embeddings
    // -------------------------------------------------------------------------

    /** Generates an embedding vector for the given text. */
    public EmbeddingResponse embed(String model, String prompt) {
        EmbeddingRequest request = new EmbeddingRequest(model, prompt);
        return httpClient.post(host + "/api/embeddings", request, EmbeddingResponse.class);
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String host = "http://localhost:11434";
        private HttpClient httpClient;

        private Builder() {}

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public OllamaClient build() {
            if (httpClient == null) httpClient = new JavaHttpClient();
            return new OllamaClient(this);
        }
    }
}