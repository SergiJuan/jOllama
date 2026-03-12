package jua.sergi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Detailed information about a specific model.
 *
 * <p>Returned by /api/show, includes architecture,
 * parameter count, quantization level and more.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelDetails {

    private String license;
    private String modelfile;
    private String parameters;
    private String template;

    @JsonProperty("model_info")
    private ModelMetadata modelInfo;

    public ModelDetails() {
    }

    public String getLicense() {
        return license;
    }

    public String getModelfile() {
        return modelfile;
    }

    public String getParameters() {
        return parameters;
    }

    public String getTemplate() {
        return template;
    }

    public ModelMetadata getModelInfo() {
        return modelInfo;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setModelfile(String modelfile) {
        this.modelfile = modelfile;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setModelInfo(ModelMetadata modelInfo) {
        this.modelInfo = modelInfo;
    }

    /**
     * Inner class holding low-level model metadata
     * returned inside the {@code model_info} field.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModelMetadata {

        @JsonProperty("general.architecture")
        private String architecture;

        @JsonProperty("general.parameter_count")
        private Long parameterCount;

        @JsonProperty("general.quantization_version")
        private Integer quantizationVersion;

        public ModelMetadata() {
        }

        public String getArchitecture() {
            return architecture;
        }

        public Long getParameterCount() {
            return parameterCount;
        }

        public Integer getQuantizationVersion() {
            return quantizationVersion;
        }

        public void setArchitecture(String architecture) {
            this.architecture = architecture;
        }

        public void setParameterCount(Long parameterCount) {
            this.parameterCount = parameterCount;
        }

        public void setQuantizationVersion(Integer quantizationVersion) {
            this.quantizationVersion = quantizationVersion;
        }
    }
}