package de.fhms.mu.worker.scheduling;

import de.fhms.mu.model.domain.scheduling.dto.ScheduleDto;
import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemDto;
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
public class SchedulingNoOpStrategyWorker {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulingNoOpStrategyWorker.class);

    @JobWorker(type = "scheduling_no-op-strategy", autoComplete = false)
    public void schedulingStrategyNoOp(
            final JobClient jobClient,
            final ActivatedJob job,
            @Variable(name = "problem") final ScheduleProblemDto problem
    ) {
        // Create empty schedule
        final var solution = ScheduleDto.builder()
                .shifts(problem.getShifts())
                .build();

        // Complete job with updated variables
        final Map<String, Object> variables = new HashMap<>();
        variables.put("solution", solution);
        jobClient.newCompleteCommand(job).variables(variables).send();
    }
}
