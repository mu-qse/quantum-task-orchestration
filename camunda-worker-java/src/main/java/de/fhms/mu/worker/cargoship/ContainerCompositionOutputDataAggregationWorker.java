package de.fhms.mu.worker.cargoship;

import de.fhms.mu.model.domain.cargoship.dto.CargoshipDto;
import de.fhms.mu.service.cargoship.ContainerCompositionOutputDataAggregationService;
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
public class ContainerCompositionOutputDataAggregationWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerCompositionOutputDataAggregationWorker.class);

    private final ContainerCompositionOutputDataAggregationService containerCompositionOutputDataAggregationService;

    @JobWorker(type = "container-composition_output-data-aggregation", autoComplete = false)
    public void containerCompositionInputDataAggregation(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "solution") final CargoshipDto solution
    ) {
        // Aggregate problem data
        final var aggregatedSolution = this.containerCompositionOutputDataAggregationService.aggregate(solution);

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("solution", aggregatedSolution);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
