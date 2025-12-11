package de.fhms.mu.service.scheduling.qaoa;

import de.fhms.mu.model.domain.scheduling.dto.AgentDto;
import de.fhms.mu.model.domain.scheduling.dto.ProblemGraphDto;
import de.fhms.mu.model.domain.scheduling.dto.ScheduleProblemDto;
import de.fhms.mu.model.domain.scheduling.dto.ShiftDto;
import de.fhms.mu.model.quantum.DataPreProcessingFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class SchedulingQaoaProblemMappingService implements DataPreProcessingFunction<ScheduleProblemDto, ProblemGraphDto<ShiftDto>> {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulingQaoaProblemMappingService.class);

    @Override
    public ProblemGraphDto<ShiftDto> map(final ScheduleProblemDto problem) {
        LOGGER.info(
                "QAOA data pre-processing: {} shifts, {} agents",
                problem.getShifts().size(),
                problem.getAgents().size()
        );

        final ProblemGraphDto<ShiftDto> problemGraph = this.createProblemGraphForMaxCut(problem); // TODO: dynamic
        LOGGER.info(
                "QAOA data pre-processing: {} nodes, {} edges",
                problemGraph.getNodes().size(),
                problemGraph.getEdges().size()
        );

        return problemGraph;
    }

    /**
     * Formalization of the problem instance as a max-cut problem.
     *
     * @param problem Problem instance
     * @return Graph that can be solved using max-cut
     */
    private ProblemGraphDto<ShiftDto> createProblemGraphForMaxCut(final ScheduleProblemDto problem) {
        final List<ShiftDto> shifts = problem.getShifts();
        final List<AgentDto> agents = problem.getAgents();

        // Add nodes for each shift-agent combination
        final ProblemGraphDto<ShiftDto> problemGraph = new ProblemGraphDto<>();
        shifts.forEach(shift -> agents.forEach(agent -> problemGraph.addNode(shift.toBuilder().agents(List.of(agent)).build())));

        final List<ShiftDto> shiftAgentCombinations = problemGraph.getNodes();
        IntStream.range(0, shiftAgentCombinations.size()).forEach(nodeIndex -> {
            final int combinationAgentIndex = nodeIndex % agents.size();

            // Add edges for one agent per shift
            IntStream.range(0, agents.size()).forEach(agentIndex -> {
                if (agentIndex != combinationAgentIndex) {
                    final int targetNodeIndex = nodeIndex - combinationAgentIndex + agentIndex;
                    problemGraph.addEdge(nodeIndex, targetNodeIndex, 1.0f);
                }
            });

            // Add edges for no consecutive shifts per agent
            final int nextNodeIndexOfAgent = nodeIndex + agents.size();
            if (nextNodeIndexOfAgent < shiftAgentCombinations.size()) {
                problemGraph.addEdge(nodeIndex, nextNodeIndexOfAgent, 1.0f);
            }
        });

        return problemGraph;
    }
}
