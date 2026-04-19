package jua.sergi.model.request;

/**
 * Request sent to /api/copy to duplicate an existing model under a new name.
 *
 * <pre>{@code
 * CopyRequest request = new CopyRequest("llama3", "llama3-backup");
 * }</pre>
 */
public class CopyRequest {

    private String source;
    private String destination;

    public CopyRequest() {}

    public CopyRequest(String source, String destination) {
        if (source == null || source.isBlank())
            throw new IllegalArgumentException("source must not be null or blank");
        if (destination == null || destination.isBlank())
            throw new IllegalArgumentException("destination must not be null or blank");
        this.source      = source;
        this.destination = destination;
    }

    public String getSource()      { return source; }
    public String getDestination() { return destination; }

    public void setSource(String source)           { this.source      = source; }
    public void setDestination(String destination) { this.destination = destination; }
}