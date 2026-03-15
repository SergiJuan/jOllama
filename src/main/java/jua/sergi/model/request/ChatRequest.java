package jua.sergi.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jua.sergi.model.Options;
import jua.sergi.model.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat request sent to /api/chat.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequest {

    private String model;
    private List<Message> messages = new ArrayList<>();
    private boolean stream = false;
    private Options options;

    public ChatRequest() {}

    public ChatRequest(String model) {
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("model must not be null or blank");
        this.model = model;
    }

    /**
     * Convenience constructor that sets a system prompt as the first message.
     */
    public ChatRequest(String model, String systemPrompt) {
        this(model);
        messages.add(new Message("system", systemPrompt));
    }

    public void addMessage(String role, String content) {
        messages.add(new Message(role, content));
    }

    public String getModel()           { return model; }
    public List<Message> getMessages() { return messages; }
    public boolean isStream()          { return stream; }
    public Options getOptions()        { return options; }

    public void setModel(String model)      { this.model   = model; }
    public void setStream(boolean stream)   { this.stream  = stream; }
    public void setOptions(Options options) { this.options = options; }
}