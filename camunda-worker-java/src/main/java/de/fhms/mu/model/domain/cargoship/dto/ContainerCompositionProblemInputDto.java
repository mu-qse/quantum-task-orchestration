package de.fhms.mu.model.domain.cargoship.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect
public class ContainerCompositionProblemInputDto {
    private int capacity;
    private int containersCount;
}
