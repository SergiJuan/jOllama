package jua.sergi.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response returned by /api/push.
 *
 * <p>When streaming is disabled, only the final status is returned.
 * Typical value is {@code "success"}.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushResponse {

    private String status;

    public PushResponse() {}

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "PushResponse{status='" + status + "'}";
    }
}