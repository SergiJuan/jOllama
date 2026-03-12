package jua.sergi;

import jua.sergi.model.GenerateResponse;
import org.junit.jupiter.api.Test;

public class OllamaClientTest {

    @Test
    public void testGenerate() {

        OllamaClient client = OllamaClient.builder().build();

        GenerateResponse response =
                client.generate("llama3", "Say hello");

        System.out.println(response.getResponse());
    }

    @Test
    public void testGenerateStreaming() {

        OllamaClient client = OllamaClient.builder().build();

        System.out.print("Response: ");

        client.generateStreaming("llama3", "Say hello", chunk -> {
            System.out.print(chunk.getResponse());
        });

        System.out.println();
    }

}