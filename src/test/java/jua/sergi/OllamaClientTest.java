package jua.sergi;

import jua.sergi.manager.ModelManager;
import jua.sergi.model.Options;
import jua.sergi.model.response.EmbeddingResponse;
import jua.sergi.model.response.GenerateResponse;
import jua.sergi.model.ModelDetails;
import jua.sergi.model.entity.ModelInfo;
import jua.sergi.model.response.PullResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class OllamaClientTest {

    @Test
    public void testGenerate() {

        OllamaClient client = OllamaClient.builder().build();

        GenerateResponse response =
                client.generate("llama3", "Say hello");

        System.out.println(response.getResponse());
    }

    @Test
    public void testGenerateWithOptions() {
        OllamaClient client = OllamaClient.builder().build();

        Options opts = Options.builder()
                .temperature(0.5)
                .numPredict(25)
                .build();

        client.generateStreaming("llama3", "Count from 1 to 5", opts, chunk -> {
            System.out.print(chunk.getResponse());
        });

        System.out.println();
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

    @Test
    public void testListModels() {

        ModelManager manager = ModelManager.builder().build();

        List<ModelInfo> models = manager.list();

        System.out.println("Installed models:");
        models.forEach(m -> System.out.println(" - " + m));
    }

    @Test
    public void testShowModel() {

        ModelManager manager = ModelManager.builder().build();

        ModelDetails details = manager.show("llama3");

        System.out.println("Parameters: " + details.getParameters());

        if (details.getModelInfo() != null) {
            System.out.println("Architecture: " + details.getModelInfo().getArchitecture());
            System.out.println("Param count:  " + details.getModelInfo().getParameterCount());
        }
    }

    @Test
    public void testPullModel() {

        ModelManager manager = ModelManager.builder().build();

        PullResponse response = manager.pull("llama3");

        System.out.println("Pull status: " + response.getStatus());
        System.out.println("Success: " + response.isSuccess());
    }

    @Test
    public void testDeleteModel() {

        ModelManager manager = ModelManager.builder().build();

        manager.delete("llama3");

        System.out.println("Model deleted successfully");
    }
}