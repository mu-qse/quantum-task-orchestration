package de.fhms.mu.model.domain.scheduling.dto;

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
public class ScheduleDto {
    private List<ShiftDto> shifts = new ArrayList<>();
}
