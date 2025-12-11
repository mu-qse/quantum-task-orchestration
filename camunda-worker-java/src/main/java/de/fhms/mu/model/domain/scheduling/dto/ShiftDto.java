package de.fhms.mu.model.domain.scheduling.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@JsonAutoDetect
public class ShiftDto {
    private String key;
    private List<AgentDto> agents = new ArrayList<>();

    @JsonIgnore
    @Override
    public String toString() {
        return String.format(
                "%s: %s",
                key,
                this.agents.stream()
                        .map(AgentDto::getName)
                        .collect(Collectors.joining(", "))
        );
    }
}
