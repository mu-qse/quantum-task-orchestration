package de.fhms.mu.service.cargoship.generator;

import de.fhms.mu.model.domain.cargoship.dto.ContainerDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ContainerGenerator {
    public List<ContainerDto> generate(final int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> ContainerDto.builder()
                        .value(10)
                        .weight(1)
                        .build())
                .collect(Collectors.toList());
    }
}
