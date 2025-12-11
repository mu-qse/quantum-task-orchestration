package de.fhms.mu.worker.cargoship;

import de.fhms.mu.model.domain.cargoship.dto.ContainerCompositionProblemInputDto;
import de.fhms.mu.service.cargoship.ContainerCompositionInputDataAggregationService;
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
public class ContainerCompositionInputDataAggregationWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerCompositionInputDataAggregationWorker.class);

    private final ContainerCompositionInputDataAggregationService containerCompositionInputDataAggregationService;

    @JobWorker(type = "container-composition_input-data-aggregation", autoComplete = false)
    public void containerCompositionInputDataAggregation(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "capacity") final Integer capacity,
            @Variable(name = "containersCount") final Integer containersCount
    ) {
        // Prepare input object
        final var problemInput = ContainerCompositionProblemInputDto.builder()
                .capacity(capacity)
                .containersCount(containersCount)
                .build();

        // Aggregate problem data
        final var problem = this.containerCompositionInputDataAggregationService.aggregate(problemInput);

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("problemInput", problemInput);
        variables.put("problem", problem);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
