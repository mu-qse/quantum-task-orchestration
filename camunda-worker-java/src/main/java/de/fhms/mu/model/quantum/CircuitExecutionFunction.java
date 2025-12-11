package de.fhms.mu.model.quantum;

public interface CircuitExecutionFunction<TInput, TOutput> {
    TOutput execute(final TInput input);
}
