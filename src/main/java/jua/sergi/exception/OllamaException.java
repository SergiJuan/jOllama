package jua.sergi.exception;

/**
 * Generic exception thrown by the Ollama client.
 */
public class OllamaException extends RuntimeException {

    public OllamaException(String message) {
        super(message);
    }

    public OllamaException(String message, Throwable cause) {
        super(message, cause);
    }

}
