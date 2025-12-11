package de.fhms.mu.service.cargoship;

import de.fhms.mu.model.OutputDataAggregationFunction;
import de.fhms.mu.model.domain.cargoship.dto.CargoshipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContainerCompositionOutputDataAggregationService implements OutputDataAggregationFunction<CargoshipDto, CargoshipDto> {

    @Override
    public CargoshipDto aggregate(final CargoshipDto context) {
        return context;
    }
}
