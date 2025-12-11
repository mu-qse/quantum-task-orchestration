package de.fhms.mu.model.quantum;

public interface CircuitOptimizationFunction<TInput, TOutput> {
    TOutput optimize(final TInput input);
}
