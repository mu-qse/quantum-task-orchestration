package de.fhms.mu.model.domain.scheduling.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect
public class ScheduleProblemInputDto {
    private int shiftsCount;
    private int agentsCount;
}
