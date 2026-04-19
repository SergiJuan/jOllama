package jua.sergi.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jua.sergi.model.entity.RunningModel;

import java.util.List;

/**
 * Response returned by /api/ps.
 *
 * <p>Contains the list of models currently loaded in memory.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PsResponse {

    private List<RunningModel> models;

    public PsResponse() {}

    public List<RunningModel> getModels() { return models; }

    public void setModels(List<RunningModel> models) { this.models = models; }
}