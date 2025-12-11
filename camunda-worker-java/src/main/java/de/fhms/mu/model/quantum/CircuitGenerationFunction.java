package de.fhms.mu.model.quantum;

public interface CircuitGenerationFunction<TInput, TOutput> {
    TOutput generate(final TInput input);
}
