package de.fhms.mu.worker.scheduling.qaoa;

import de.fhms.mu.model.domain.scheduling.dto.ProblemGraphDto;
import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemDto;
import de.fhms.mu.model.domain.scheduling.dto.ShiftDto;
import de.fhms.mu.model.quantum.CircuitExecutionResultDto;
import de.fhms.mu.service.scheduling.qaoa.SchedulingQaoaSolutionMappingContext;
import de.fhms.mu.service.scheduling.qaoa.SchedulingQaoaSolutionMappingService;
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
public class SchedulingQaoaSolutionMappingWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulingQaoaSolutionMappingWorker.class);

    private final SchedulingQaoaSolutionMappingService mappingService;

    @JobWorker(type = "scheduling_qaoa_solution-mapping", autoComplete = false)
    public void solutionMapping(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "problem") final ScheduleProblemDto problem,
            @Variable(name = "problemGraph") final ProblemGraphDto<ShiftDto> problemGraph,
            @Variable(name = "circuitExecutionResult") final CircuitExecutionResultDto circuitExecutionResult
    ) {
        // Prepare context object
        final SchedulingQaoaSolutionMappingContext solutionMappingContext = new SchedulingQaoaSolutionMappingContext(
                problem,
                problemGraph,
                circuitExecutionResult
        );

        final var solution = this.mappingService.map(solutionMappingContext);

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("solution", solution);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
