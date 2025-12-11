package de.fhms.mu.worker.scheduling;

import de.fhms.mu.config.SchedulingConfiguration;
import de.fhms.mu.model.domain.scheduling.SchedulingStrategyEnum;
import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemDto;
import de.fhms.mu.service.scheduling.SchedulingStrategySelectionService;
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
public class SchedulingStrategySelectionWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulingStrategySelectionWorker.class);

    private final SchedulingConfiguration schedulingConfiguration;
    private final SchedulingStrategySelectionService schedulingStrategySelectionService;

    @JobWorker(type = "scheduling_strategy-selection", autoComplete = false)
    public void schedulingStrategySelection(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "problem") final ScheduleProblemDto problem,
            @Variable(name = "preferredStrategyKey") final String preferredStrategyKey
    ) {
        final var preferredStrategy = this.getPreferredSchedulingStrategyByKey(preferredStrategyKey);

        // Select strategy
        final var selectedStrategy = this.schedulingStrategySelectionService.select(problem, preferredStrategy);

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("preferredStrategyKey", preferredStrategy.toString());
        variables.put("selectedStrategyKey", selectedStrategy.toString());
        variables.put("selectedStrategyProcessId", selectedStrategy.getProcessId());
        jobClient.newCompleteCommand(job).variables(variables).send();
    }

    public SchedulingStrategyEnum getPreferredSchedulingStrategyByKey(final String preferredStrategyKey) {
        var preferredStrategy = SchedulingStrategyEnum.fromString(preferredStrategyKey);
        if (preferredStrategy == null || preferredStrategy == SchedulingStrategyEnum.DEFAULT) {
            preferredStrategy = this.schedulingConfiguration.getDefaultStrategy();
        }
        return preferredStrategy;
    }
}
