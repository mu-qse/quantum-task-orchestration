package de.fhms.mu.service.cargoship.qaoa;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.fhms.mu.model.domain.cargoship.dto.ContainerCompositionProblemDto;
import de.fhms.mu.model.quantum.CircuitExecutionResultDto;

@JsonAutoDetect
public record ContainerCompositionQaoaSolutionMappingContext(ContainerCompositionProblemDto problem, CircuitExecutionResultDto executionResult) {

}
