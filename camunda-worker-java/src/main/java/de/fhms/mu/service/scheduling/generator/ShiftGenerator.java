package de.fhms.mu.service.scheduling.generator;

import de.fhms.mu.model.domain.scheduling.dto.ShiftDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ShiftGenerator {
    private static final List<String> SHIFT_KEYS = List.of("Mo", "Tu", "We", "Th", "Fr");

    public List<ShiftDto> generate(final int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> ShiftDto.builder()
                        .key(SHIFT_KEYS.get(i % SHIFT_KEYS.size()))
                        .build())
                .collect(Collectors.toList());
    }
}
