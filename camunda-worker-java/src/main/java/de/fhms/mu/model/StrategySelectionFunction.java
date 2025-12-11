package de.fhms.mu.model;

public interface StrategySelectionFunction<TInput, TEnum> {
    TEnum select(final TInput problem, final TEnum preferredStrategy);
}
