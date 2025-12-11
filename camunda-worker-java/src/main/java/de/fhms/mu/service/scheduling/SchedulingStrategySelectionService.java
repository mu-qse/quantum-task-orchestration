package de.fhms.mu.service.scheduling;

import de.fhms.mu.config.SchedulingConfiguration;
import de.fhms.mu.model.StrategySelectionFunction;
import de.fhms.mu.model.domain.scheduling.SchedulingStrategyEnum;
import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulingStrategySelectionService implements StrategySelectionFunction<ScheduleProblemDto, SchedulingStrategyEnum> {

    private final SchedulingConfiguration schedulingConfiguration;

    @Override
    public SchedulingStrategyEnum select(final ScheduleProblemDto problem, final SchedulingStrategyEnum preferredStrategy) {
        // Check if the given preferred strategy is available
        if (!this.schedulingConfiguration.isStrategyAvailable(preferredStrategy)) {
            return SchedulingStrategyEnum.UNKNOWN;
        }

        // Complex selection logic goes here

        // Return the preferred strategy
        return preferredStrategy;
    }
}
