package de.fhms.mu.worker.cargoship.qaoa;

import de.fhms.mu.model.domain.cargoship.dto.ContainerCompositionProblemDto;
import de.fhms.mu.model.domain.cargoship.dto.ContainerDto;
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
public class ContainerCompositionQaoaProblemMappingWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerCompositionQaoaProblemMappingWorker.class);

    @JobWorker(type = "knapsack_qaoa_problem-mapping", autoComplete = false)
    public void problemMapping(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "problem") final ContainerCompositionProblemDto problem
    ) {
        final var capacity = problem.getCapacity();
        final var weights = problem.getContainers().stream().map(ContainerDto::getWeight).toList();
        final var values = problem.getContainers().stream().map(ContainerDto::getValue).toList();

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("capacity", capacity);
        variables.put("weights", weights);
        variables.put("values", values);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
