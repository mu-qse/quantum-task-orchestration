package de.fhms.mu.model;

public interface OutputDataAggregationFunction<TOutput, TContext> {
    TOutput aggregate(final TContext context);
}
