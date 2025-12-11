package de.fhms.mu.model.quantum.qaoa;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.fhms.mu.model.domain.scheduling.dto.ProblemGraphDto;
import de.fhms.mu.model.domain.scheduling.dto.ShiftDto;

@JsonAutoDetect
public record QaoaCircuitEnrichmentContext(Long processInstanceId, ProblemGraphDto<ShiftDto> problemGraph,
                                           String circuit) {

}
