package jua.sergi.manager;

import jua.sergi.http.HttpClient;
import jua.sergi.http.JavaHttpClient;
import jua.sergi.model.ModelDetails;
import jua.sergi.model.entity.ModelInfo;
import jua.sergi.model.entity.RunningModel;
import jua.sergi.model.request.CopyRequest;
import jua.sergi.model.request.CreateRequest;
import jua.sergi.model.request.PullRequest;
import jua.sergi.model.request.PushRequest;
import jua.sergi.model.response.CreateResponse;
import jua.sergi.model.response.ModelListResponse;
import jua.sergi.model.response.PsResponse;
import jua.sergi.model.response.PullResponse;
import jua.sergi.model.response.PushResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages Ollama models: list, pull, create, copy, push, delete, show and ps.
 *
 * <p>Use {@link #builder()} to create and configure an instance:</p>
 *
 * <pre>{@code
 * ModelManager manager = ModelManager.builder().build();
 *
 * manager.list().forEach(System.out::println);
 * manager.pull("llama3");
 * manager.create("my-model", "FROM llama3\nSYSTEM You are a pirate.");
 * manager.copy("llama3", "llama3-backup");
 * manager.push("my-namespace/my-model");
 * manager.ps().forEach(System.out::println);
 * manager.delete("llama3");
 * }</pre>
 */
public class ModelManager {

    private final String host;
    private final HttpClient httpClient;

    ModelManager(String host, HttpClient httpClient) {
        this.host       = host;
        this.httpClient = httpClient;
    }

    // -------------------------------------------------------------------------
    // Existing endpoints
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // New endpoints — 0.0.4
    // -------------------------------------------------------------------------

    /**
     * Creates a new model from a Modelfile.
     *
     * <p>Calls POST /api/create. This is a blocking call.</p>
     *
     * <pre>{@code
     * CreateResponse r = manager.create("my-model",
     *         "FROM llama3\nSYSTEM You are a helpful pirate.");
     * System.out.println(r.getStatus()); // "success"
     * }</pre>
     *
     * @param name      name to give the new model
     * @param modelfile Modelfile contents as a string
     * @return response with status "success" if completed
     */
    public CreateResponse create(String name, String modelfile) {
        return create(new CreateRequest(name, modelfile));
    }

    /**
     * Creates a new model from a {@link CreateRequest}.
     *
     * @param request fully configured create request
     * @return response with status "success" if completed
     */
    public CreateResponse create(CreateRequest request) {
        return httpClient.post(
                host + "/api/create",
                request,
                CreateResponse.class
        );
    }

    /**
     * Copies an existing model to a new name.
     *
     * <p>Calls POST /api/copy. Returns normally if successful;
     * throws {@link jua.sergi.exception.OllamaException} on error.</p>
     *
     * <pre>{@code
     * manager.copy("llama3", "llama3-backup");
     * }</pre>
     *
     * @param source      name of the model to copy
     * @param destination name for the new copy
     */
    public void copy(String source, String destination) {
        httpClient.post(
                host + "/api/copy",
                new CopyRequest(source, destination),
                Void.class
        );
    }

    /**
     * Pushes a model to the Ollama registry.
     *
     * <p>Calls POST /api/push. This is a blocking call.</p>
     *
     * <pre>{@code
     * PushResponse r = manager.push("my-namespace/my-model");
     * System.out.println(r.getStatus()); // "success"
     * }</pre>
     *
     * @param modelName name of the model to push (e.g. "my-namespace/my-model")
     * @return response with status "success" if completed
     */
    public PushResponse push(String modelName) {
        return httpClient.post(
                host + "/api/push",
                new PushRequest(modelName),
                PushResponse.class
        );
    }

    /**
     * Returns the list of models currently loaded in memory (VRAM/RAM).
     *
     * <p>Calls GET /api/ps.</p>
     *
     * <pre>{@code
     * manager.ps().forEach(System.out::println);
     * // llama3 (4.1 GB) — expires: 2025-04-19T18:00:00Z
     * }</pre>
     *
     * @return list of running models
     */
    public List<RunningModel> ps() {
        PsResponse response = httpClient.get(
                host + "/api/ps",
                PsResponse.class
        );
        return response.getModels();
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    /**
     * Creates a new builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

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