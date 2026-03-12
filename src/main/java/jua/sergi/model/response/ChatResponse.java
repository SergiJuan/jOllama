package jua.sergi.model.response;

import jua.sergi.model.entity.Message;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response returned from /api/chat.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {

    private Message message;
    private boolean done;

    public Message getMessage() {
        return message;
    }

    public boolean isDone() {
        return done;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
