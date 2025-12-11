package de.fhms.mu.model.quantum;

public interface DataPostProcessingFunction<TInput, TOutput> {
    TOutput map(final TInput input);
}
