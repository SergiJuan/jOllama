package jua.sergi.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response returned by /api/pull.
 *
 * <p>When streaming is disabled, only the final status
 * is returned. Typical value is {@code "success"}.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullResponse {

    private String status;

    public PullResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }
}