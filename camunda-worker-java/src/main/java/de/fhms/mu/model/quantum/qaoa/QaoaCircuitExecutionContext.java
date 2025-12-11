package de.fhms.mu.model.quantum.qaoa;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public record QaoaCircuitExecutionContext(Long processInstanceId, String optimizedCircuit) {

}
