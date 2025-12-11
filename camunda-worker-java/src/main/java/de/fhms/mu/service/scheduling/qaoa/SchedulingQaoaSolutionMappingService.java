package de.fhms.mu.service.scheduling.qaoa;

import de.fhms.mu.model.domain.scheduling.dto.ScheduleDto;
import de.fhms.mu.model.domain.scheduling.dto.ShiftDto;
import de.fhms.mu.model.quantum.DataPostProcessingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class SchedulingQaoaSolutionMappingService implements DataPostProcessingFunction<SchedulingQaoaSolutionMappingContext, ScheduleDto> {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulingQaoaSolutionMappingService.class);

    @Override
    public ScheduleDto map(final SchedulingQaoaSolutionMappingContext postProcessingContext) {
        LOGGER.info(
                "QAOA data post-processing ..."
        );

        final ScheduleDto solution = this.createScheduleFromCircuitExecutionResult(postProcessingContext);
        LOGGER.info(
                "QAOA data post-processing: {}",
                solution.getShifts().toString()
        );
        return solution;
    }

    private ScheduleDto createScheduleFromCircuitExecutionResult(final SchedulingQaoaSolutionMappingContext postProcessingContext) {
        final Optional<String> mostFrequentState = postProcessingContext.executionResult().getMostFrequentState();
        if (mostFrequentState.isEmpty()) {
            throw new RuntimeException("Missing most frequent state.");
        }

        final String bitstring = mostFrequentState.get();
        final List<ShiftDto> problemGraphNodes = postProcessingContext.problemGraph().getNodes();
        if (bitstring.length() != problemGraphNodes.size()) {
            throw new UnsupportedOperationException(String.format("Bitstring does not match nodes: %d != %d", bitstring.length(), problemGraphNodes.size()));
        }

        final List<ShiftDto> shifts = IntStream.range(0, bitstring.length())
                .filter(bitIndex -> bitstring.charAt(bitIndex) == '1')
                .mapToObj(problemGraphNodes::get)
                .toList();
        return ScheduleDto.builder()
                .shifts(shifts)
                .build();
    }

}
