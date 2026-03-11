package jua.sergi.http;

/**
 * Simple abstraction for HTTP clients.
 * Allows custom implementations if needed.
 */
public interface HttpClient {

    <T> T post(String url, Object body, Class<T> responseType);

}
