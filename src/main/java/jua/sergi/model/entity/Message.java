package jua.sergi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a chat message in a conversation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    
    private String role;
    private String content;
    private String thinking;

    public Message() {}

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public String getThinking() {
        return thinking;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setThinking(String thinking) {
        this.thinking = thinking;
    }
}
