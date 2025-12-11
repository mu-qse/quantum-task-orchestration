package de.fhms.mu.model.domain.scheduling;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum SchedulingStrategyEnum {
    UNKNOWN("unknown", null),
    DEFAULT("default", null),
    NO_OP("no-op", "callcenter-scheduling_no-op-strategy"),
    QAOA("qaoa", "callcenter-scheduling_qaoa-strategy");

    private final String value;
    private final String processId;

    @Override
    public String toString() {
        return value;
    }

    public static SchedulingStrategyEnum fromString(final String value) {
        return Arrays.stream(SchedulingStrategyEnum.values())
                .filter(e -> e.toString().equals(value))
                .findFirst()
                .orElse(SchedulingStrategyEnum.UNKNOWN);
    }
}
