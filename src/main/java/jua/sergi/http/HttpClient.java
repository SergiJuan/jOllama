package jua.sergi.http;

import java.util.function.Consumer;

/**
 * Simple abstraction for HTTP clients.
 */
public interface HttpClient {

    <T> T post(String url, Object body, Class<T> responseType);

    <T> void stream(String url, Object body, Class<T> responseType, Consumer<T> onChunk);


}
