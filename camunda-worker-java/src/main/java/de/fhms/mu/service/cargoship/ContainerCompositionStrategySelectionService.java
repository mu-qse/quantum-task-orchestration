package de.fhms.mu.service.cargoship;

import de.fhms.mu.config.ContainerCompositionConfiguration;
import de.fhms.mu.model.StrategySelectionFunction;
import de.fhms.mu.model.domain.cargoship.ContainerCompositionStrategyEnum;
import de.fhms.mu.model.domain.cargoship.dto.ContainerCompositionProblemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContainerCompositionStrategySelectionService implements StrategySelectionFunction<ContainerCompositionProblemDto, ContainerCompositionStrategyEnum> {

    private final ContainerCompositionConfiguration containerCompositionConfiguration;

    @Override
    public ContainerCompositionStrategyEnum select(final ContainerCompositionProblemDto problem, final ContainerCompositionStrategyEnum preferredStrategy) {
        // Check if the given preferred strategy is available
        if (!this.containerCompositionConfiguration.isStrategyAvailable(preferredStrategy)) {
            return ContainerCompositionStrategyEnum.UNKNOWN;
        }

        // Complex selection logic goes here

        // Return the preferred strategy
        return preferredStrategy;
    }
}
