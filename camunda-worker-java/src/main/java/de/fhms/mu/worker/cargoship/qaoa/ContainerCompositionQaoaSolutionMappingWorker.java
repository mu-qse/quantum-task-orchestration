package de.fhms.mu.worker.cargoship.qaoa;

import de.fhms.mu.model.domain.cargoship.dto.ContainerCompositionProblemDto;
import de.fhms.mu.model.quantum.CircuitExecutionResultDto;
import de.fhms.mu.service.cargoship.qaoa.ContainerCompositionQaoaSolutionMappingContext;
import de.fhms.mu.service.cargoship.qaoa.ContainerCompositionQaoaSolutionMappingService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ContainerCompositionQaoaSolutionMappingWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerCompositionQaoaSolutionMappingWorker.class);

    private final ContainerCompositionQaoaSolutionMappingService mappingService;

    @JobWorker(type = "knapsack_qaoa_solution-mapping", autoComplete = false)
    public void solutionMapping(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "problem") final ContainerCompositionProblemDto problem,
            @Variable(name = "circuitExecutionResult") final CircuitExecutionResultDto circuitExecutionResult
    ) {
        // Prepare context object
        final var solutionMappingContext = new ContainerCompositionQaoaSolutionMappingContext(
                problem,
                circuitExecutionResult
        );

        final var solution = this.mappingService.map(solutionMappingContext);

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("solution", solution);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
