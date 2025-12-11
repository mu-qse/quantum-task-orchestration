package de.fhms.mu.model.domain.cargoship.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect
public class CargoshipDto {
    private List<ContainerDto> containers = new ArrayList<>();
}
