package jua.sergi.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jua.sergi.model.entity.ModelInfo;

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