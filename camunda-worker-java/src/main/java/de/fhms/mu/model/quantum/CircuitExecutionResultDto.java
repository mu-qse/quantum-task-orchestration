package de.fhms.mu.model.quantum;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonAutoDetect
public class CircuitExecutionResultDto {
    private int shots = 0;
    private Map<String, Integer> states = new HashMap<>();

    public float getStateProbability(final String state) {
        return (float) this.states.get(state) / this.shots;
    }

    @JsonIgnore
    public Optional<String> getMostFrequentState() {
        return this.states.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public static int getStateIndex(final String state) {
        final String reversed = new StringBuilder(state).reverse().toString();
        return Integer.parseInt(reversed, 2);
    }
}
