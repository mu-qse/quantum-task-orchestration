import sys
import os

import qiskit
from qiskit_aer import AerSimulator
from qiskit_aer.primitives import SamplerV2

import pennylane as qml

from util import graph_utils
from util import circuit_utils
from util import bitstring_utils
from util import device_utils

# Constants
DEVICE_PROVIDERS = {
    "statevector": lambda conf: AerSimulator(
        method = "statevector",
        shots = conf["shots"],
    ),
    "pennylane": lambda conf: qml.device(
        'default.qubit',
        wires = 12,
        shots = conf["shots"],
    )
}
DEVICE_PROVIDERS["default"] = DEVICE_PROVIDERS["statevector"]

def execute_circuit(circuit: qiskit.QuantumCircuit, device_configuration: dict):
    # Device instantiation
    key = device_configuration["key"]
    shots = device_configuration["shots"]
    device = DEVICE_PROVIDERS[key](device_configuration)

    # Circuit execution
    sampler = SamplerV2.from_backend(device)
    state_counts = circuit_utils.run_circuit(circuit, sampler, shots)

    return {
        "shots": shots,
        "states": state_counts,
    }

def execute_circuit_pennylane(circuit, device_configuration: dict):
    # Device instantiation
    key = "pennylane" # device_configuration["key"]
    shots = device_configuration["shots"]
    device = DEVICE_PROVIDERS[key](device_configuration)

    # Circuit execution
    state_counts = circuit_utils.run_circuit_pennylane(circuit, device)

    return {
        "shots": shots,
        "states": state_counts,
    }

if __name__ == '__main__':
    # Get parameters from cli args
    temp_dir_path = sys.argv[1] if len(sys.argv) > 1 else "./"
    #problem_graph_path = os.path.join(temp_dir_path, "problem-graph.json")
    enriched_circuit_path = os.path.join(temp_dir_path, "circuit-enriched.qasm")
    execution_result_path = os.path.join(temp_dir_path, "execution-result.json")
    selected_device_path = os.path.join(temp_dir_path, "selected-device.json")

    # Read input
    #problem_graph = graph_utils.import_graph(problem_graph_path)
    enriched_circuit = circuit_utils.import_circuit_qasm2(enriched_circuit_path)
    #enriched_circuit = circuit_utils.import_circuit_qasm_pennylane(enriched_circuit_path)
    selected_device = device_utils.import_configuration(selected_device_path)

    # Circuit execution
    execution_result = execute_circuit(enriched_circuit, selected_device)
    #execution_result = execute_circuit_pennylane(enriched_circuit, selected_device)

    # Measurements
    bitstring_utils.export_execution_result(execution_result, execution_result_path)
