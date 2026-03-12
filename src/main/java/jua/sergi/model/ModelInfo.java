package jua.sergi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a locally available Ollama model.
 *
 * <p>Returned as part of the {@code /api/tags} response
 * when listing installed models.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelInfo {

    private String name;
    private String digest;
    private long size;

    @JsonProperty("modified_at")
    private String modifiedAt;

    public ModelInfo() {
    }

    public String getName() {
        return name;
    }

    public String getDigest() {
        return digest;
    }

    public long getSize() {
        return size;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    /**
     * Returns the model size formatted as MB or GB.
     *
     * @return human-readable size string
     */
    public String getFormattedSize() {
        if (size >= 1_000_000_000L) {
            return String.format("%.1f GB", size / 1_000_000_000.0);
        } else {
            return String.format("%.1f MB", size / 1_000_000.0);
        }
    }

    @Override
    public String toString() {
        return name + " (" + getFormattedSize() + ")";
    }
}