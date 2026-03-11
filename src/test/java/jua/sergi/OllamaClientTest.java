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

}
