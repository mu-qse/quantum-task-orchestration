package de.fhms.mu.config;

import de.fhms.mu.model.domain.scheduling.SchedulingStrategyEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Getter
public class SchedulingConfiguration {

    private final ApplicationContext context;

    @Value("${scheduling.strategy.default}")
    private String defaultStrategyKey;

    private final List<SchedulingStrategyEnum> availableStrategies = List.of(
            SchedulingStrategyEnum.NO_OP,
            SchedulingStrategyEnum.QAOA
    );

    public SchedulingStrategyEnum getDefaultStrategy() {
        return SchedulingStrategyEnum.fromString(this.defaultStrategyKey);
    }

    public boolean isStrategyAvailable(final SchedulingStrategyEnum strategy) {
        if (strategy == SchedulingStrategyEnum.DEFAULT) {
            return this.isStrategyAvailable(this.getDefaultStrategy());
        }

        return this.availableStrategies.contains(strategy);
    }

}
