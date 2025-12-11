package de.fhms.mu.worker.scheduling;

import de.fhms.mu.model.domain.scheduling.dto.ScheduleDto;
import de.fhms.mu.service.scheduling.SchedulingOutputDataAggregationService;
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
public class SchedulingOutputDataAggregationWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulingOutputDataAggregationWorker.class);

    private final SchedulingOutputDataAggregationService schedulingOutputDataAggregationService;

    @JobWorker(type = "scheduling_output-data-aggregation", autoComplete = false)
    public void schedulingInputDataAggregation(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "solution") final ScheduleDto solution
    ) {
        // Aggregate problem data
        final var aggregatedSolution = this.schedulingOutputDataAggregationService.aggregate(solution);

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("solution", aggregatedSolution);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
