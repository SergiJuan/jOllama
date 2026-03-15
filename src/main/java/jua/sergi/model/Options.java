package jua.sergi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Optional generation parameters shared by generate and chat requests.
 *
 * <p>All fields are optional. Null values are excluded from serialization
 * so Ollama uses its own defaults when not specified.</p>
 *
 * <pre>{@code
 * Options opts = Options.builder()
 *         .temperature(0.7)
 *         .topP(0.9)
 *         .seed(42)
 *         .build();
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Options {

    private Double temperature;
    private Double topP;
    private Double topK;
    private Integer seed;
    private Integer numPredict;
    private Double repeatPenalty;
    private Double presencePenalty;
    private Double frequencyPenalty;
    private Integer numCtx;
    private Double mirostat;
    private Double mirostatEta;
    private Double mirostatTau;
    private Integer numGpu;
    private String stop;

    private Options() {}

    public Double getTemperature()      { return temperature; }
    public Double getTopP()             { return topP; }
    public Double getTopK()             { return topK; }
    public Integer getSeed()            { return seed; }
    public Integer getNumPredict()      { return numPredict; }
    public Double getRepeatPenalty()    { return repeatPenalty; }
    public Double getPresencePenalty()  { return presencePenalty; }
    public Double getFrequencyPenalty() { return frequencyPenalty; }
    public Integer getNumCtx()          { return numCtx; }
    public Double getMirostat()         { return mirostat; }
    public Double getMirostatEta()      { return mirostatEta; }
    public Double getMirostatTau()      { return mirostatTau; }
    public Integer getNumGpu()          { return numGpu; }
    public String getStop()             { return stop; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Options opts = new Options();

        /** Controls randomness. Range: 0.0 (deterministic) – 2.0 (very random). Default: 0.8 */
        public Builder temperature(double temperature) {
            opts.temperature = temperature;
            return this;
        }

        /** Nucleus sampling. Keeps tokens whose cumulative probability >= topP. Default: 0.9 */
        public Builder topP(double topP) {
            opts.topP = topP;
            return this;
        }

        /** Top-K sampling. Limits to the K most probable tokens. Default: 40 */
        public Builder topK(double topK) {
            opts.topK = topK;
            return this;
        }

        /** Random seed for reproducibility. 0 means random. */
        public Builder seed(int seed) {
            opts.seed = seed;
            return this;
        }

        /** Maximum tokens to generate. -1 means unlimited. */
        public Builder numPredict(int numPredict) {
            opts.numPredict = numPredict;
            return this;
        }

        /** Penalises repeated tokens. Values > 1.0 reduce repetition. Default: 1.1 */
        public Builder repeatPenalty(double repeatPenalty) {
            opts.repeatPenalty = repeatPenalty;
            return this;
        }

        /** Penalises tokens already present in the text so far. */
        public Builder presencePenalty(double presencePenalty) {
            opts.presencePenalty = presencePenalty;
            return this;
        }

        /** Penalises tokens proportionally to how often they have appeared. */
        public Builder frequencyPenalty(double frequencyPenalty) {
            opts.frequencyPenalty = frequencyPenalty;
            return this;
        }

        /** Context window size in tokens. Default: 2048 */
        public Builder numCtx(int numCtx) {
            opts.numCtx = numCtx;
            return this;
        }

        /** Mirostat sampling mode: 0 = disabled, 1 = Mirostat, 2 = Mirostat 2.0 */
        public Builder mirostat(double mirostat) {
            opts.mirostat = mirostat;
            return this;
        }

        /** Mirostat learning rate. Default: 0.1 */
        public Builder mirostatEta(double mirostatEta) {
            opts.mirostatEta = mirostatEta;
            return this;
        }

        /** Mirostat target entropy. Default: 5.0 */
        public Builder mirostatTau(double mirostatTau) {
            opts.mirostatTau = mirostatTau;
            return this;
        }

        /** Number of GPU layers to use. */
        public Builder numGpu(int numGpu) {
            opts.numGpu = numGpu;
            return this;
        }

        /** Stop sequence — generation halts when this string is produced. */
        public Builder stop(String stop) {
            opts.stop = stop;
            return this;
        }

        public Options build() {
            return opts;
        }
    }
}