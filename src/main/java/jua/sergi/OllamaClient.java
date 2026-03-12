package jua.sergi;

import jua.sergi.http.HttpClient;
import jua.sergi.http.JavaHttpClient;
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
 * <p>This class exposes high level methods for interacting
 * with Ollama models such as text generation, chat and embeddings.</p>
 *
 * <p>Use {@link #builder()} to create and configure an instance:</p>
 *
 * <pre>{@code
 * OllamaClient client = OllamaClient.builder()
 *         .host("http://localhost:11434")
 *         .build();
 * }</pre>
 */
public class OllamaClient {

    private final String host;
    private final HttpClient httpClient;

    private OllamaClient(Builder builder) {
        this.host = builder.host;
        this.httpClient = builder.httpClient;
    }

    // -------------------------------------------------------------------------
    // API methods
    // -------------------------------------------------------------------------

    /**
     * Generates text using the Ollama /api/generate endpoint.
     *
     * <p>Blocking call — waits for the full response before returning.
     * For token-by-token output use {@link #generateStreaming} instead.</p>
     *
     * @param model  model name (e.g. "llama3")
     * @param prompt prompt text
     * @return complete generation response
     */
    public GenerateResponse generate(String model, String prompt) {
        GenerateRequest request = new GenerateRequest(model, prompt, false);

        return httpClient.post(
                host + "/api/generate",
                request,
                GenerateResponse.class
        );
    }

    /**
     * Generates text with streaming via /api/generate.
     *
     * <p>Each token is delivered to the {@code onChunk} consumer
     * as it arrives. The final chunk will have {@code isDone() == true}.</p>
     *
     * <pre>{@code
     * client.generateStreaming("llama3", "Tell me a story", chunk -> {
     *     System.out.print(chunk.getResponse());
     * });
     * }</pre>
     *
     * @param model   model name (e.g. "llama3")
     * @param prompt  prompt text
     * @param onChunk consumer called for each received token chunk
     */
    public void generateStreaming(String model, String prompt, Consumer<GenerateResponse> onChunk) {
        GenerateRequest request = new GenerateRequest(model, prompt, true);

        httpClient.stream(
                host + "/api/generate",
                request,
                GenerateResponse.class,
                onChunk
        );
    }

    /**
    * Sends a chat request to Ollama via /api/chat with streaming.
    *
    * <p>Each token is delivered to the {@code onChunk} consumer
    * as it arrives. The final chunk will have {@code isDone() == true}.</p>
    *
    * <pre>{@code
    * client.chatStreaming(request, chunk -> {
    *     System.out.print(chunk.getMessage().getContent());
    * });
    * }</pre>
    *
    * @param request chat request with model and message history
    * @param onChunk consumer called for each received token chunk
    */
    public void chatStreaming(ChatRequest request, Consumer<ChatResponse> onChunk) {
        request.setStream(true);
        httpClient.stream(
                host + "/api/chat",
                request,
                ChatResponse.class,
                onChunk
        );
    }

    /**
     * Sends a chat request to Ollama via /api/chat.
     *
     * @param request chat request with model and message history
     * @return chat response containing the assistant's reply
     */
    public ChatResponse chat(ChatRequest request) {
        return httpClient.post(
                host + "/api/chat",
                request,
                ChatResponse.class
        );
    }

    /**
     * Generates an embedding vector for the given text via /api/embeddings.
     *
     * <p>Embeddings are numeric vectors that represent the semantic meaning
     * of a text. Useful for similarity search, RAG pipelines,
     * clustering and classification tasks.</p>
     *
     * <pre>{@code
     * EmbeddingResponse r1 = client.embed("llama3", "The cat sat on the mat");
     * EmbeddingResponse r2 = client.embed("llama3", "A cat was on the rug");
     *
     * double similarity = r1.cosineSimilarity(r2);
     * System.out.println("Similarity: " + similarity); // close to 1.0
     * }</pre>
     *
     * @param model  model name (e.g. "llama3")
     * @param prompt text to embed
     * @return embedding response containing the vector
     */
    public EmbeddingResponse embed(String model, String prompt) {
        EmbeddingRequest request = new EmbeddingRequest(model, prompt);

        return httpClient.post(
                host + "/api/embeddings",
                request,
                EmbeddingResponse.class
        );
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    /**
     * Creates a new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder used to configure and create an {@link OllamaClient}.
     *
     * <pre>{@code
     * OllamaClient client = OllamaClient.builder()
     *         .host("http://my-server:11434")
     *         .httpClient(myCustomHttpClient)
     *         .build();
     * }</pre>
     */
    public static class Builder {

        private String host = "http://localhost:11434";
        private HttpClient httpClient;

        private Builder() {
        }

        /**
         * Sets the Ollama server host. Defaults to {@code http://localhost:11434}.
         *
         * @param host server base URL
         * @return this builder
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * Sets a custom {@link HttpClient} implementation.
         * Useful for testing or custom networking needs.
         *
         * @param httpClient custom HTTP client
         * @return this builder
         */
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * Builds and returns a configured {@link OllamaClient}.
         *
         * @return new OllamaClient instance
         */
        public OllamaClient build() {
            if (httpClient == null) {
                httpClient = new JavaHttpClient();
            }
            return new OllamaClient(this);
        }
    }
}
