import sys
import os

import numpy as np
import networkx as nx

import qiskit
from qiskit_aer import AerSimulator
from qiskit_aer.primitives import EstimatorV2

from util import graph_utils
from util import circuit_utils
from util import qaoa_utils

# Constants
circuit_layers = 2
simulator_method = "statevector"

def refine_circuit_maxcut(
        circuit: qiskit.QuantumCircuit,
        problem_graph: nx.Graph,
) -> qiskit.QuantumCircuit:
    # Device instantiation
    device = AerSimulator(method = simulator_method)
    estimator = EstimatorV2.from_backend(device)

    # Circuit transpilation
    transpiled_circuit = circuit_utils.transpile_circuit(circuit, device)

    # Circuit optimization
    cost_hamiltonian = qaoa_utils.build_cost_hamiltonian_maxcut(problem_graph)
    initial_params = np.array([np.pi, np.pi / 2] * circuit_layers)
    optimized_params, _ = qaoa_utils.optimize_circuit(transpiled_circuit, cost_hamiltonian, initial_params, estimator)
    return transpiled_circuit.assign_parameters(optimized_params)

def refine_circuit_knapsack(
        circuit: qiskit.QuantumCircuit,
        values: list[int],
        weights: list[int],
        capacity: int,
) -> qiskit.QuantumCircuit:
    # Device instantiation
    device = AerSimulator(method = simulator_method)
    estimator = EstimatorV2.from_backend(device)

    # Circuit transpilation
    transpiled_circuit = circuit_utils.transpile_circuit(circuit, device)

    # Circuit optimization
    cost_hamiltonian = qaoa_utils.build_cost_hamiltonian_knapsack(values, weights, capacity)
    initial_params = np.array([np.pi, np.pi / 2] * circuit_layers)
    optimized_params, _ = qaoa_utils.optimize_circuit(transpiled_circuit, cost_hamiltonian, initial_params, estimator)
    return transpiled_circuit.assign_parameters(optimized_params)

if __name__ == '__main__':
    # Get parameters from cli args
    temp_dir_path = sys.argv[1] if len(sys.argv) > 1 else "./"
    problem_graph_path = os.path.join(temp_dir_path, "problem-graph.json")
    circuit_path = os.path.join(temp_dir_path, "circuit.qasm")
    enriched_circuit_path = os.path.join(temp_dir_path, "circuit-enriched.qasm")

    # Read input
    problem_graph = graph_utils.import_graph(problem_graph_path)
    circuit = circuit_utils.import_circuit_qasm3(circuit_path)

    # Circuit enrichment
    enriched_circuit = refine_circuit_maxcut(circuit, problem_graph)

    #values = [ 3, 4, 5, 6, 7 ]
    #weights = [ 2, 3, 4, 5, 6 ]
    #capacity = 10
    #enriched_circuit = refine_circuit_knapsack(circuit, values, weights, capacity)

    # Circuit export
    circuit_utils.export_circuit_qasm2(enriched_circuit, enriched_circuit_path)
