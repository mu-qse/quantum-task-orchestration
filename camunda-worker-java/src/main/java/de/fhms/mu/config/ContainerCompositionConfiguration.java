package de.fhms.mu.config;

import de.fhms.mu.model.domain.cargoship.ContainerCompositionStrategyEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Getter
public class ContainerCompositionConfiguration {

    private final ApplicationContext context;

    @Value("${container-composition.strategy.default}")
    private String defaultStrategyKey;

    private final List<ContainerCompositionStrategyEnum> availableStrategies = List.of(
            ContainerCompositionStrategyEnum.NO_OP,
            ContainerCompositionStrategyEnum.QAOA
    );

    public ContainerCompositionStrategyEnum getDefaultStrategy() {
        return ContainerCompositionStrategyEnum.fromString(this.defaultStrategyKey);
    }

    public boolean isStrategyAvailable(final ContainerCompositionStrategyEnum strategy) {
        if (strategy == ContainerCompositionStrategyEnum.DEFAULT) {
            return this.isStrategyAvailable(this.getDefaultStrategy());
        }

        return this.availableStrategies.contains(strategy);
    }

}
