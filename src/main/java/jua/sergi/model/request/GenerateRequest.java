package jua.sergi.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jua.sergi.model.Options;

/**
 * Request sent to /api/generate.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenerateRequest {

    private String model;
    private String prompt;
    private String system;
    private boolean stream = false;
    private Options options;

    public GenerateRequest() {}

    public GenerateRequest(String model, String prompt, boolean stream) {
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("model must not be null or blank");
        this.model  = model;
        this.prompt = prompt;
        this.stream = stream;
    }

    public String getModel()      { return model; }
    public String getPrompt()     { return prompt; }
    public String getSystem()     { return system; }
    public boolean isStream()     { return stream; }
    public Options getOptions()   { return options; }

    public void setModel(String model)       { this.model   = model; }
    public void setPrompt(String prompt)     { this.prompt  = prompt; }
    public void setSystem(String system)     { this.system  = system; }
    public void setStream(boolean stream)    { this.stream  = stream; }
    public void setOptions(Options options)  { this.options = options; }
}