package de.fhms.mu.service.scheduling;

import de.fhms.mu.model.OutputDataAggregationFunction;
import de.fhms.mu.model.domain.scheduling.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulingOutputDataAggregationService implements OutputDataAggregationFunction<ScheduleDto, ScheduleDto> {

    @Override
    public ScheduleDto aggregate(final ScheduleDto context) {
        return context;
    }
}
