package jua.sergi;

import jua.sergi.http.HttpClient;
import jua.sergi.model.ChatRequest;
import jua.sergi.model.ChatResponse;
import jua.sergi.model.GenerateRequest;
import jua.sergi.model.GenerateResponse;

import java.util.function.Consumer;

/**
 * Main client used to communicate with the Ollama API.
 *
 * <p>This class exposes high level methods for interacting
 * with Ollama models such as text generation and chat.</p>
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
     * <p>This is a blocking call that waits for the complete response
     * before returning. For real-time token-by-token output, use
     * {@link #generateStreaming(String, String, Consumer)} instead.</p>
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
     * Generates text using the Ollama /api/generate endpoint with streaming.
     *
     * <p>Ollama sends partial responses as newline-delimited JSON (NDJSON).
     * Each chunk is delivered to the {@code onChunk} consumer as it arrives,
     * allowing real-time display of tokens as they are generated.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * client.generateStreaming("llama3", "Tell me a story", chunk -> {
     *     System.out.print(chunk.getResponse());
     * });
     * }</pre>
     *
     * @param model   model name
     * @param prompt  prompt text
     * @param onChunk consumer called for each received token chunk;
     *                the final chunk will have {@code isDone() == true}
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
     * Sends a chat request to Ollama.
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
     * Creates a new builder instance.
     */
    public static OllamaClientBuilder builder() {
        return new OllamaClientBuilder();
    }
}