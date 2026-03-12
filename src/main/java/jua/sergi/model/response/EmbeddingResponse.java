package jua.sergi.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Response returned by /api/embeddings.
 *
 * <p>Contains a numeric vector representing the semantic
 * meaning of the input text. The vector size depends on
 * the model used.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbeddingResponse {

    private List<Double> embedding;

    public EmbeddingResponse() {
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    /**
     * Computes the cosine similarity between this embedding
     * and another one. Returns a value between -1 and 1,
     * where 1 means identical meaning.
     *
     * @param other another embedding response
     * @return cosine similarity score
     */
    public double cosineSimilarity(EmbeddingResponse other) {
        List<Double> a = this.embedding;
        List<Double> b = other.getEmbedding();

        if (a.size() != b.size()) {
            throw new IllegalArgumentException(
                    "Embeddings must have the same size to compare: "
                            + a.size() + " vs " + b.size()
            );
        }

        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < a.size(); i++) {
            dot   += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}