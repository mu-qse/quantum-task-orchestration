package de.fhms.mu.service.cargoship;

import de.fhms.mu.model.InputDataAggregationFunction;
import de.fhms.mu.model.domain.cargoship.dto.ContainerCompositionProblemDto;
import de.fhms.mu.model.domain.cargoship.dto.ContainerCompositionProblemInputDto;
import de.fhms.mu.service.cargoship.generator.ContainerGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContainerCompositionInputDataAggregationService implements InputDataAggregationFunction<ContainerCompositionProblemInputDto, ContainerCompositionProblemDto> {

    private final ContainerGenerator containerGenerator;

    @Override
    public ContainerCompositionProblemDto aggregate(final ContainerCompositionProblemInputDto input) {
        final var capacity = input.getCapacity();
        final var containers = this.containerGenerator.generate(input.getContainersCount());
        return ContainerCompositionProblemDto.builder()
                .capacity(capacity)
                .containers(containers)
                .build();
    }
}
