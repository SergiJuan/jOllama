package jua.sergi.model.request;

/**
 * Request sent to /api/push to upload a model to the Ollama registry.
 *
 * <pre>{@code
 * PushRequest request = new PushRequest("my-namespace/my-model");
 * }</pre>
 */
public class PushRequest {

    private String name;
    private boolean stream = false;

    public PushRequest() {}

    public PushRequest(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name must not be null or blank");
        this.name = name;
    }

    public String getName()   { return name; }
    public boolean isStream() { return stream; }

    public void setName(String name)        { this.name   = name; }
    public void setStream(boolean stream)   { this.stream = stream; }
}