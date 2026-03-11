package jua.sergi.model;

/**
 * Response returned from /api/chat.
 */
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