package jua.sergi;

import jua.sergi.http.HttpClient;
import jua.sergi.model.ChatRequest;
import jua.sergi.model.ChatResponse;
import jua.sergi.model.EmbeddingRequest;
import jua.sergi.model.EmbeddingResponse;
import jua.sergi.model.GenerateRequest;
import jua.sergi.model.GenerateResponse;

import java.util.function.Consumer;

/**
 * Main client used to communicate with the Ollama API.
 *
 * <p>This class exposes high level methods for interacting
 * with Ollama models such as text generation, chat and embeddings.</p>
 *
 * <p>Use {@link #builder()} to create and configure an instance.</p>
 */
public class OllamaClient {

    private final String host;
    private final HttpClient httpClient;

    OllamaClient(String host, HttpClient httpClient) {
        this.host = host;
        this.httpClient = httpClient;
    }

    /**
     * Generates text using the Ollama /api/generate endpoint.
     *
     * @param model  model name
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
     * @param model   model name
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
     * of a text. They are useful for similarity search, RAG pipelines,
     * clustering, and classification tasks.</p>
     *
     * <pre>{@code
     * EmbeddingResponse r1 = client.embed("llama3", "The cat sat on the mat");
     * EmbeddingResponse r2 = client.embed("llama3", "A cat was on the rug");
     *
     * double similarity = r1.cosineSimilarity(r2);
     * System.out.println("Similarity: " + similarity); // close to 1.0
     * }</pre>
     *
     * @param model  model name
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

    /**
     * Creates a new builder instance.
     */
    public static OllamaClientBuilder builder() {
        return new OllamaClientBuilder();
    }
}