package de.fhms.mu.worker.scheduling;

import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemInputDto;
import de.fhms.mu.service.scheduling.SchedulingInputDataAggregationService;
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
public class SchedulingInputDataAggregationWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulingInputDataAggregationWorker.class);

    private final SchedulingInputDataAggregationService schedulingInputDataAggregationService;

    @JobWorker(type = "scheduling_input-data-aggregation", autoComplete = false)
    public void schedulingInputDataAggregation(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "shiftsCount") final Integer shiftsCount,
            @Variable(name = "agentsCount") final Integer agentsCount
    ) {
        // Prepare input object
        final var problemInput = ScheduleProblemInputDto.builder()
                .shiftsCount(shiftsCount)
                .agentsCount(agentsCount)
                .build();

        // Aggregate problem data
        final var problem = this.schedulingInputDataAggregationService.aggregate(problemInput);

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("problemInput", problemInput);
        variables.put("problem", problem);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
