package de.fhms.mu.model;

public interface InputDataAggregationFunction<TInput, TContext> {
    TContext aggregate(final TInput input);
}
