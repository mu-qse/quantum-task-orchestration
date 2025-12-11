package de.fhms.mu.worker.cargoship;

import de.fhms.mu.config.ContainerCompositionConfiguration;
import de.fhms.mu.model.domain.cargoship.ContainerCompositionStrategyEnum;
import de.fhms.mu.model.domain.cargoship.dto.ContainerCompositionProblemDto;
import de.fhms.mu.service.cargoship.ContainerCompositionStrategySelectionService;
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
public class ContainerCompositionStrategySelectionWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerCompositionStrategySelectionWorker.class);

    private final ContainerCompositionConfiguration containerCompositionConfiguration;
    private final ContainerCompositionStrategySelectionService containerCompositionStrategySelectionService;

    @JobWorker(type = "container-composition_strategy-selection", autoComplete = false)
    public void containerCompositionStrategySelection(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "problem") final ContainerCompositionProblemDto problem,
            @Variable(name = "preferredStrategyKey") final String preferredStrategyKey
    ) {
        final var preferredStrategy = this.getPreferredContainerCompositionStrategyByKey(preferredStrategyKey);

        // Select strategy
        final var selectedStrategy = this.containerCompositionStrategySelectionService.select(problem, preferredStrategy);

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("preferredStrategyKey", preferredStrategy.toString());
        variables.put("selectedStrategyKey", selectedStrategy.toString());
        variables.put("selectedStrategyProcessId", selectedStrategy.getProcessId());
        jobClient.newCompleteCommand(job).variables(variables).send();
    }

    public ContainerCompositionStrategyEnum getPreferredContainerCompositionStrategyByKey(final String preferredStrategyKey) {
        var preferredStrategy = ContainerCompositionStrategyEnum.fromString(preferredStrategyKey);
        if (preferredStrategy == null || preferredStrategy == ContainerCompositionStrategyEnum.DEFAULT) {
            preferredStrategy = this.containerCompositionConfiguration.getDefaultStrategy();
        }
        return preferredStrategy;
    }
}
