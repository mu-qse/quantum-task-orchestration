import qiskit
import qiskit.qasm2
import qiskit.qasm3
import qiskit.primitives
from qiskit.providers import Backend

import numpy as np

import pennylane as qml


def export_circuit_qasm2(circuit: qiskit.QuantumCircuit, path: str) -> str:
    circuit_qasm = qiskit.qasm2.dumps(circuit)
    with open(path, "w", encoding = "utf8") as file:
        file.write(circuit_qasm)
    return circuit_qasm

def export_circuit_qasm3(circuit: qiskit.QuantumCircuit, path: str) -> str:
    circuit_qasm = qiskit.qasm3.dumps(circuit)
    with open(path, "w", encoding = "utf8") as file:
        file.write(circuit_qasm)
    return circuit_qasm

def import_circuit_qasm2(path: str) -> qiskit.QuantumCircuit:
    with open(path, "r", encoding = "utf8") as file:
        return qiskit.qasm2.loads(file.read(), custom_instructions = qiskit.qasm2.LEGACY_CUSTOM_INSTRUCTIONS)

def import_circuit_qasm3(path: str) -> qiskit.QuantumCircuit:
    with open(path, "r", encoding = "utf8") as file:
        return qiskit.qasm3.loads(file.read())

def transpile_circuit(circuit: qiskit.QuantumCircuit, backend: Backend, optimization_level: int = 0):
    # pm = generate_preset_pass_manager(optimization_level = 3, backend = backend)
    # return pm.run(circuit)
    return qiskit.transpile(circuit, backend, optimization_level)

def run_circuit(
        circuit: qiskit.QuantumCircuit,
        sampler: qiskit.primitives.BaseSamplerV2,
        shots: int = 1000,
):
    job = sampler.run([ circuit ], shots = shots)
    result = job.result()[0]
    state_counts = result.data.meas.get_counts()
    state_counts_sorted = dict(sorted(state_counts.items(), key = lambda key_val: key_val[1], reverse = True))
    return state_counts_sorted

def import_circuit_qasm_pennylane(path: str):
    with open(path, "r", encoding = "utf8") as file:
        return qml.from_qasm(file.read())

def run_circuit_pennylane(
        circuit,
        device,
        shots: int = 1000,
):
    print(qml.draw(circuit)())

    @qml.qnode(device)
    def get_probs():
        circuit()
        return qml.probs(wires = device.wires)

    #qnode = qml.QNode(circuit, device)
    probs = get_probs()
    states = get_states(probs, len(device.wires))

    print(probs)
    print(states)

    return dict({
        "shots": shots,
        "states": states
    })

def get_states(probs, wires: int):
    sorted_probs = np.argsort(probs)[::-1]
    return [{
        'index': index.item(),
        'bitstring': '{0:b}'.format(index).zfill(wires),
        'prob': probs[index],
    } for index in sorted_probs]

# OPENQASM_GATES = {
#     "CNOT": "cx",
#     "CZ": "cz",
#     "U3": "u3",
#     "U2": "u2",
#     "U1": "u1",
#     "Identity": "id",
#     "PauliX": "x",
#     "PauliY": "y",
#     "PauliZ": "z",
#     "Hadamard": "h",
#     "S": "s",
#     "Adjoint(S)": "sdg",
#     "T": "t",
#     "Adjoint(T)": "tdg",
#     "RX": "rx",
#     "RY": "ry",
#     "RZ": "rz",
#     "CRX": "crx",
#     "CRY": "cry",
#     "CRZ": "crz",
#     "SWAP": "swap",
#     "Toffoli": "ccx",
#     "CSWAP": "cswap",
#     "PhaseShift": "u1",
# }
