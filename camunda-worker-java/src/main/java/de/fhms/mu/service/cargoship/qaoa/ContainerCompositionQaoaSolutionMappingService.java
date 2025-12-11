package de.fhms.mu.service.cargoship.qaoa;

import de.fhms.mu.model.domain.cargoship.dto.CargoshipDto;
import de.fhms.mu.model.domain.cargoship.dto.ContainerDto;
import de.fhms.mu.model.quantum.DataPostProcessingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class ContainerCompositionQaoaSolutionMappingService implements DataPostProcessingFunction<ContainerCompositionQaoaSolutionMappingContext, CargoshipDto> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerCompositionQaoaSolutionMappingService.class);

    @Override
    public CargoshipDto map(final ContainerCompositionQaoaSolutionMappingContext postProcessingContext) {
        LOGGER.info(
                "QAOA data post-processing ..."
        );

        final CargoshipDto solution = this.createCargoshipFromCircuitExecutionResult(postProcessingContext);
        LOGGER.info(
                "QAOA data post-processing: {}",
                solution.getContainers().toString()
        );
        return solution;
    }

    private CargoshipDto createCargoshipFromCircuitExecutionResult(final ContainerCompositionQaoaSolutionMappingContext postProcessingContext) {
        final Optional<String> mostFrequentState = postProcessingContext.executionResult().getMostFrequentState();
        if (mostFrequentState.isEmpty()) {
            throw new RuntimeException("Missing most frequent state.");
        }

        final String bitstring = mostFrequentState.get();

        final var availableContainers = postProcessingContext.problem().getContainers();
        final var availableContainersCount = availableContainers.size();
        final List<ContainerDto> containers = IntStream.range(0, availableContainersCount)
                .filter(bitIndex -> bitstring.charAt(bitIndex) == '1')
                .mapToObj(availableContainers::get)
                .toList();
        return CargoshipDto.builder()
                .containers(containers)
                .build();
    }

}
