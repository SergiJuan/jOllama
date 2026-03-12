package jua.sergi;

import jua.sergi.http.HttpClient;
import jua.sergi.http.JavaHttpClient;
import jua.sergi.model.ModelDetails;
import jua.sergi.model.ModelInfo;
import jua.sergi.model.ModelListResponse;
import jua.sergi.model.PullRequest;
import jua.sergi.model.PullResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages Ollama models: list, pull, delete and show info.
 *
 * <p>Use {@link #builder()} to create and configure an instance:</p>
 *
 * <pre>{@code
 * ModelManager manager = ModelManager.builder().build();
 *
 * manager.list().forEach(m -> System.out.println(m));
 * manager.pull("llama3");
 * manager.delete("llama3");
 * }</pre>
 */
public class ModelManager {

    private final String host;
    private final HttpClient httpClient;

    ModelManager(String host, HttpClient httpClient) {
        this.host = host;
        this.httpClient = httpClient;
    }

    /**
     * Returns the list of models currently installed locally.
     *
     * <p>Calls GET /api/tags.</p>
     *
     * @return list of installed models
     */
    public List<ModelInfo> list() {
        ModelListResponse response = httpClient.get(
                host + "/api/tags",
                ModelListResponse.class
        );
        return response.getModels();
    }

    /**
     * Downloads a model from the Ollama registry.
     *
     * <p>Calls POST /api/pull. This is a blocking call
     * that waits until the model is fully downloaded.</p>
     *
     * @param modelName model name to pull (e.g. "llama3", "mistral")
     * @return pull response with status "success" if completed
     */
    public PullResponse pull(String modelName) {
        PullRequest request = new PullRequest(modelName);

        return httpClient.post(
                host + "/api/pull",
                request,
                PullResponse.class
        );
    }

    /**
     * Deletes a locally installed model.
     *
     * <p>Calls DELETE /api/delete.</p>
     *
     * @param modelName model name to delete (e.g. "llama3")
     */
    public void delete(String modelName) {
        Map<String, String> body = new HashMap<>();
        body.put("name", modelName);

        httpClient.delete(host + "/api/delete", body);
    }

    /**
     * Returns detailed information about a specific model.
     *
     * <p>Calls POST /api/show. Includes architecture,
     * parameter count, quantization, modelfile and more.</p>
     *
     * @param modelName model name (e.g. "llama3")
     * @return detailed model information
     */
    public ModelDetails show(String modelName) {
        Map<String, String> body = new HashMap<>();
        body.put("name", modelName);

        return httpClient.post(
                host + "/api/show",
                body,
                ModelDetails.class
        );
    }

    /**
     * Creates a new builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public static class Builder {

        private String host = "http://localhost:11434";
        private HttpClient httpClient;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public ModelManager build() {
            if (httpClient == null) {
                httpClient = new JavaHttpClient();
            }
            return new ModelManager(host, httpClient);
        }
    }
}