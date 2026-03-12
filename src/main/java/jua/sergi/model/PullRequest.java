package jua.sergi.model;

/**
 * Request sent to /api/pull to download a model.
 */
public class PullRequest {

    private String name;
    private boolean stream = false;

    public PullRequest() {
    }

    public PullRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isStream() {
        return stream;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}