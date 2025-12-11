package de.fhms.mu.worker.scheduling.qaoa;

import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemDto;
import de.fhms.mu.service.scheduling.qaoa.SchedulingQaoaProblemMappingService;
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
public class SchedulingQaoaProblemMappingWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulingQaoaProblemMappingWorker.class);

    private final SchedulingQaoaProblemMappingService mappingService;

    @JobWorker(type = "scheduling_qaoa_problem-mapping", autoComplete = false)
    public void problemMapping(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "problem") final ScheduleProblemDto problem
    ) {
        // Transform problem instance to graph-based format
        final var problemGraph = this.mappingService.map(problem);

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("problemGraph", problemGraph);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
