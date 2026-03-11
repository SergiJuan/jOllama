package jua.sergi;

import jua.sergi.http.HttpClient;
import jua.sergi.model.ChatRequest;
import jua.sergi.model.ChatResponse;
import jua.sergi.model.GenerateRequest;
import jua.sergi.model.GenerateResponse;

/**
 * Main client used to communicate with the Ollama API.
 *
 * This class exposes high level methods for interacting
 * with Ollama models such as text generation and chat.
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
     * @return generation response
     */
    public GenerateResponse generate(String model, String prompt) {

        GenerateRequest request = new GenerateRequest(model, prompt);

        return httpClient.post(
                host + "/api/generate",
                request,
                GenerateResponse.class
        );
    }

    /**
     * Sends a chat request to Ollama.
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
