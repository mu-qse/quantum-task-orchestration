package de.fhms.mu.worker.cargoship;

import de.fhms.mu.model.domain.cargoship.dto.CargoshipDto;
import de.fhms.mu.model.domain.cargoship.dto.ContainerCompositionProblemDto;
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
public class ContainerCompositionNoOpStrategyWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerCompositionNoOpStrategyWorker.class);

    @JobWorker(type = "container-composition_no-op-strategy", autoComplete = false)
    public void containerCompositionStrategyNoOp(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "problem") final ContainerCompositionProblemDto problem
    ) {
        // Create empty cargoship
        final var solution = CargoshipDto.builder()
                .build();

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("solution", solution);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
