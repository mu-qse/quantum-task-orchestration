package de.fhms.mu.service.scheduling.qaoa;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.fhms.mu.model.domain.scheduling.dto.ProblemGraphDto;
import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemDto;
import de.fhms.mu.model.domain.scheduling.dto.ShiftDto;
import de.fhms.mu.model.quantum.CircuitExecutionResultDto;

@JsonAutoDetect
public record SchedulingQaoaSolutionMappingContext(ScheduleProblemDto problem, ProblemGraphDto<ShiftDto> problemGraph,
                                                   CircuitExecutionResultDto executionResult) {

}
