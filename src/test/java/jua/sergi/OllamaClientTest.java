package jua.sergi;

import jua.sergi.model.EmbeddingResponse;
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

    @Test
    public void testEmbed() {

        OllamaClient client = OllamaClient.builder().build();

        EmbeddingResponse r1 = client.embed("llama3", "The cat sat on the mat");
        EmbeddingResponse r2 = client.embed("llama3", "A cat was resting on the rug");
        EmbeddingResponse r3 = client.embed("llama3", "Java is a programming language");

        System.out.println("Vector size: " + r1.getEmbedding().size());

        double sim12 = r1.cosineSimilarity(r2);
        double sim13 = r1.cosineSimilarity(r3);

        System.out.printf("Similarity (cat vs cat):  %.4f%n", sim12);
        System.out.printf("Similarity (cat vs java): %.4f%n", sim13);
    }

}