package de.fhms.mu.model.domain.scheduling.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@JsonAutoDetect
public class AgentDto {
    private String name;

    @JsonIgnore
    @Override
    public String toString() {
        return this.name;
    }
}
