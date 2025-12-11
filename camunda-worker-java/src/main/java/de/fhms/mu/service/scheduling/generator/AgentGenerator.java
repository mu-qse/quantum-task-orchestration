package de.fhms.mu.service.scheduling.generator;

import de.fhms.mu.model.domain.scheduling.dto.AgentDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AgentGenerator {
    private static final List<String> AGENT_NAMES = List.of("Alice", "Bob", "Charlie", "David", "Emily");

    public List<AgentDto> generate(final int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> AgentDto.builder()
                        .name(AGENT_NAMES.get(i % AGENT_NAMES.size()))
                        .build())
                .collect(Collectors.toList());
    }
}
