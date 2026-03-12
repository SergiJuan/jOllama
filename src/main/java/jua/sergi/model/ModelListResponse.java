package jua.sergi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Response returned by /api/tags.
 *
 * <p>Contains the list of models currently available locally.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelListResponse {

    private List<ModelInfo> models;

    public ModelListResponse() {
    }

    public List<ModelInfo> getModels() {
        return models;
    }

    public void setModels(List<ModelInfo> models) {
        this.models = models;
    }
}