package de.fhms.mu.model.domain.scheduling.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonAutoDetect
public class ProblemGraphDto<TNode> {
    private List<TNode> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    public void addNode(final TNode node) {
        this.nodes.add((node));
    }

    public void addEdge(final int source, final int target, final float weight) {
        final Optional<Edge> existingEdge = this.getEdge(source, target);
        existingEdge.ifPresent(edge -> this.edges.remove(edge));

        this.edges.add(new Edge(source, target, weight));
    }

    private Optional<Edge> getEdge(final int source, final int target) {
        return this.edges.stream()
                .filter(edge -> edge.source == source && edge.target == target || edge.source == target && edge.target == source)
                .findFirst();
    }

    record Edge(int source, int target, float weight) {
    }
}
