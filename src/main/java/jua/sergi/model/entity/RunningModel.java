package jua.sergi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a model currently loaded in memory (VRAM/RAM).
 *
 * <p>Returned as part of the {@code /api/ps} response.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunningModel {

    private String name;
    private String model;
    private long size;
    private String digest;

    @JsonProperty("expires_at")
    private String expiresAt;

    public RunningModel() {}

    public String getName()      { return name; }
    public String getModel()     { return model; }
    public long getSize()        { return size; }
    public String getDigest()    { return digest; }
    public String getExpiresAt() { return expiresAt; }

    public void setName(String name)           { this.name      = name; }
    public void setModel(String model)         { this.model     = model; }
    public void setSize(long size)             { this.size      = size; }
    public void setDigest(String digest)       { this.digest    = digest; }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }

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
        return name + " (" + getFormattedSize() + ") — expires: " + expiresAt;
    }
}