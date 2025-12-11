package de.fhms.mu.model.domain.cargoship.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect
public class ContainerCompositionProblemDto {
    private int capacity;
    private List<ContainerDto> containers;
}
