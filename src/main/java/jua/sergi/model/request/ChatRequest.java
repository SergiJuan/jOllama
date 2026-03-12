package jua.sergi.model.request;

import jua.sergi.model.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat request sent to /api/chat.
 */
public class ChatRequest {

    private String model;
    private List<Message> messages = new ArrayList<>();

    public ChatRequest() {}

    public ChatRequest(String model) {
        this.model = model;
    }

    public void addMessage(String role, String content) {
        messages.add(new Message(role, content));
    }

    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
