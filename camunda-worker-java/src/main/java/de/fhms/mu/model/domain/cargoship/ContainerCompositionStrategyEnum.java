package de.fhms.mu.model.domain.cargoship;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ContainerCompositionStrategyEnum {
    UNKNOWN("unknown", null),
    DEFAULT("default", null),
    NO_OP("no-op", "container-composition_no-op-strategy"),
    QAOA("qaoa", "container-composition_qaoa-strategy");

    private final String value;
    private final String processId;

    @Override
    public String toString() {
        return value;
    }

    public static ContainerCompositionStrategyEnum fromString(final String value) {
        return Arrays.stream(ContainerCompositionStrategyEnum.values())
                .filter(e -> e.toString().equals(value))
                .findFirst()
                .orElse(ContainerCompositionStrategyEnum.UNKNOWN);
    }
}
