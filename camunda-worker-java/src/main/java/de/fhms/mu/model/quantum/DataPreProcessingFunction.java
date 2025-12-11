package de.fhms.mu.model.quantum;

public interface DataPreProcessingFunction<TInput, TOutput> {
    TOutput map(final TInput input);
}
