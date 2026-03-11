package jua.sergi.model;

/**
 * Request sent to /api/generate.
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
