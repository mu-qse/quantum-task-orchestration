import networkx as nx
import numpy as np
import rustworkx as rx
from qiskit_algorithms import NumPyMinimumEigensolver
from qiskit_optimization.algorithms import MinimumEigenOptimizer
from qiskit_optimization.converters import QuadraticProgramToQubo
from scipy.optimize import minimize

import qiskit.primitives
from qiskit.quantum_info import SparsePauliOp
from qiskit.circuit.library import QAOAAnsatz
from qiskit.quantum_info.operators.linear_op import LinearOp

from qiskit_optimization.applications import Knapsack

from util import graph_utils


# Max-Cut

def build_circuit_maxcut(
        graph: nx.Graph,
        layers: int,
) -> tuple[QAOAAnsatz, LinearOp]:
    cost_hamiltonian = build_cost_hamiltonian_maxcut(graph)
    circuit = QAOAAnsatz(cost_hamiltonian, layers, flatten = True)
    circuit.measure_all()
    return circuit, cost_hamiltonian

def build_cost_hamiltonian_maxcut(graph: nx.Graph) -> LinearOp:
    converted_graph = graph_utils.convert_nx_to_rx(graph)
    pauli_operators: list[tuple[str, float]] = []
    for edge in list(converted_graph.edge_list()):
        paulis = ["I"] * len(converted_graph)
        paulis[edge[0]], paulis[edge[1]] = "Z", "Z"
        weight = converted_graph.get_edge_data(edge[0], edge[1])["weight"]
        pauli_operators.append(("".join(paulis)[::-1], weight))
    return SparsePauliOp.from_list(pauli_operators)

# Knapsack

def build_circuit_knapsack(
        values: list[int],
        weights: list[int],
        capacity: int,
        layers: int,
) -> tuple[QAOAAnsatz, LinearOp]:
    cost_hamiltonian = build_cost_hamiltonian_knapsack(values, weights, capacity)
    circuit = QAOAAnsatz(cost_operator = cost_hamiltonian, reps = layers, flatten = True)
    circuit.measure_all()
    return circuit, cost_hamiltonian

def build_cost_hamiltonian_knapsack(
        values: list[int],
        weights: list[int],
        capacity: int,
) -> LinearOp:
    prob = Knapsack(values, weights, capacity)
    qp = prob.to_quadratic_program()
    cubo = QuadraticProgramToQubo().convert(qp)

    meo = MinimumEigenOptimizer(min_eigen_solver = NumPyMinimumEigensolver())
    result = meo.solve(cubo)
    print(result.prettyprint())
    print(result.samples)
    print("\nsolution:", prob.interpret(result))

    ising, offset = cubo.to_ising() # There's just one element in this list - don't know why
    print(f"qubits: {ising.num_qubits}, offset: {offset}")
    return ising

# Algorithm-independent

def optimize_circuit(
        circuit: qiskit.QuantumCircuit,
        cost_hamiltonian: LinearOp,
        init_params: np.ndarray,
        estimator: qiskit.primitives.BaseEstimatorV2,
        tol: float = 1e-10,
):
    cost_values = []
    isa_hamiltonian = cost_hamiltonian.apply_layout(circuit.layout)

    def estimator_func(params: list[float]):
        pub = (circuit, isa_hamiltonian, params)
        job = estimator.run([ pub ])
        print(params)
        result = job.result()[0]
        cost = result.data.evs
        print(cost)
        cost_values.append(cost)
        return cost

    result = minimize(
        estimator_func,
        init_params,
        method = "COBYLA",
        tol = tol,
    )

    return result.x, cost_values
