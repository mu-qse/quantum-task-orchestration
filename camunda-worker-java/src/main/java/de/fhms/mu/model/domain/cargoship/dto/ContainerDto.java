package de.fhms.mu.model.domain.cargoship.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@JsonAutoDetect
public class ContainerDto {
    private int value;
    private int weight;

    @JsonIgnore
    @Override
    public String toString() {
        return String.format("(value: %d, weight: %d)", this.value, this.weight);
    }
}
