package jua.sergi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Response returned by /api/generate.
 *
 * This class can represent either a single response
 * or multiple partial responses concatenated.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateResponse {

    private String response;
    private boolean done;

    public GenerateResponse() {
    }

    public GenerateResponse(String response, boolean done) {
        this.response = response;
        this.done = done;
    }

    public String getResponse() {
        return response;
    }

    public boolean isDone() {
        return done;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Combines multiple partial responses into one full response.
     */
    public static GenerateResponse fromPartialResponses(List<GenerateResponse> partials) {
        StringBuilder full = new StringBuilder();
        for (GenerateResponse partial : partials) {
            if (partial.getResponse() != null) {
                full.append(partial.getResponse());
            }
        }
        return new GenerateResponse(full.toString(), true);
    }
}