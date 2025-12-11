import sys
import os

import networkx as nx

from util import graph_utils
from util import circuit_utils
from util import qaoa_utils

# Constants
circuit_layers = 2

def generate_circuit_maxcut(problem_graph: nx.Graph):
    circuit, _ = qaoa_utils.build_circuit_maxcut(problem_graph, circuit_layers)
    return circuit

def generate_circuit_knapsack(values: list[int], weights: list[int], capacity: int):
    circuit, _ = qaoa_utils.build_circuit_knapsack(values, weights, capacity, circuit_layers)
    return circuit

if __name__ == '__main__':
    # Get parameters from cli args
    temp_dir_path = sys.argv[1] if len(sys.argv) > 1 else "./"
    problem_graph_path = os.path.join(temp_dir_path, "problem-graph.json")
    circuit_path = os.path.join(temp_dir_path, "circuit.qasm")

    # Read input
    problem_graph = graph_utils.import_graph(problem_graph_path)

    # Circuit generation
    circuit = generate_circuit_maxcut(problem_graph)

    #values = [ 3, 4, 5, 6, 7 ]
    #weights = [ 2, 3, 4, 5, 6 ]
    #capacity = 10
    #circuit = generate_circuit_knapsack(values, weights, capacity)

    # Circuit export
    circuit_utils.export_circuit_qasm3(circuit, circuit_path)
