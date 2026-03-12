package jua.sergi.model.request;

/**
 * Request sent to /api/generate.
 *
 * <p>Set {@code stream = true} when using
 * {@link jua.sergi.OllamaClient#generateStreaming} to receive
 * partial responses as they are generated.</p>
 */
public class GenerateRequest {

    private String model;
    private String prompt;
    private boolean stream = false;

    public GenerateRequest() {
    }

    public GenerateRequest(String model, String prompt) {
        this.model = model;
        this.prompt = prompt;
    }

    public GenerateRequest(String model, String prompt, boolean stream) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
    }

    public String getModel() {
        return model;
    }

    public String getPrompt() {
        return prompt;
    }

    public boolean isStream() {
        return stream;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}