package de.fhms.mu.service.scheduling;

import de.fhms.mu.model.InputDataAggregationFunction;
import de.fhms.mu.model.domain.scheduling.dto.AgentDto;
import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemDto;
import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemInputDto;
import de.fhms.mu.model.domain.scheduling.dto.ShiftDto;
import de.fhms.mu.service.scheduling.generator.AgentGenerator;
import de.fhms.mu.service.scheduling.generator.ShiftGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulingInputDataAggregationService implements InputDataAggregationFunction<ScheduleProblemInputDto, ScheduleProblemDto> {

    private final ShiftGenerator shiftGenerator;
    private final AgentGenerator agentGenerator;

    @Override
    public ScheduleProblemDto aggregate(final ScheduleProblemInputDto input) {
        final List<ShiftDto> shifts = this.shiftGenerator.generate(input.getShiftsCount());
        final List<AgentDto> agents = this.agentGenerator.generate(input.getAgentsCount());
        return ScheduleProblemDto.builder()
                .shifts(shifts)
                .agents(agents)
                .build();
    }
}
