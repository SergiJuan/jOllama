package jua.sergi;


import jua.sergi.http.HttpClient;
import jua.sergi.http.JavaHttpClient;

/**
 * Builder used to configure and create an OllamaClient.
 */
public class OllamaClientBuilder {

    private String host = "http://localhost:11434";
    private HttpClient httpClient;

    public OllamaClientBuilder host(String host) {
        this.host = host;
        return this;
    }

    public OllamaClientBuilder httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public OllamaClient build() {

        if (httpClient == null) {
            httpClient = new JavaHttpClient();
        }

        return new OllamaClient(host, httpClient);
    }

}
