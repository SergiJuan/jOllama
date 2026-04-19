package jua.sergi.model.request;

/**
 * Request sent to /api/create to build a new model from a Modelfile.
 *
 * <pre>{@code
 * CreateRequest request = new CreateRequest("my-model",
 *         "FROM llama3\nSYSTEM You are a helpful pirate.");
 * }</pre>
 */
public class CreateRequest {

    private String name;
    private String modelfile;
    private boolean stream = false;

    public CreateRequest() {}

    public CreateRequest(String name, String modelfile) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name must not be null or blank");
        if (modelfile == null || modelfile.isBlank())
            throw new IllegalArgumentException("modelfile must not be null or blank");
        this.name      = name;
        this.modelfile = modelfile;
    }

    public String getName()      { return name; }
    public String getModelfile() { return modelfile; }
    public boolean isStream()    { return stream; }

    public void setName(String name)           { this.name      = name; }
    public void setModelfile(String modelfile) { this.modelfile = modelfile; }
    public void setStream(boolean stream)      { this.stream    = stream; }
}