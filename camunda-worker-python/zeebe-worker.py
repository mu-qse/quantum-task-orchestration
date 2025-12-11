import sys
import asyncio

from pyzeebe import ZeebeWorker, Job, create_insecure_channel

from networkx.readwrite import json_graph
import qiskit.qasm3
import qiskit.qasm2

from qaoa_circuit_generation import generate_circuit_maxcut
from qaoa_circuit_refinement import refine_circuit_maxcut

from qaoa_circuit_generation import generate_circuit_knapsack
from qaoa_circuit_refinement import refine_circuit_knapsack

from quantum_device_selection import select_device
from quantum_circuit_execution import execute_circuit


async def main():
    grpc_address = f"{sys.argv[1]}:26500" if len(sys.argv) > 1 else "localhost:26500"
    print(f"Connecting to {grpc_address} ...")
    channel = create_insecure_channel(grpc_address = grpc_address)

    scheduling_qaoa_circuit_generation_worker = ZeebeWorker(channel)
    scheduling_qaoa_circuit_refinement_worker = ZeebeWorker(channel)

    knapsack_qaoa_circuit_generation_worker = ZeebeWorker(channel)
    knapsack_qaoa_circuit_refinement_worker = ZeebeWorker(channel)

    circuit_execution_worker = ZeebeWorker(channel)
    device_selection_worker = ZeebeWorker(channel)

    # QAOA for Scheduling

    @scheduling_qaoa_circuit_generation_worker.task(task_type = "scheduling_qaoa_circuit-generation")
    async def qaoa_circuit_generation(job: Job):
        print(f"Scheduling circuit generation started: {job.process_instance_key}")

        problem_graph = json_graph.node_link_graph(job.variables["problemGraph"], edges = "edges")
        circuit = generate_circuit_maxcut(problem_graph)
        circuit_qasm = qiskit.qasm3.dumps(circuit)

        print(f"Scheduling circuit generation finished: {len(circuit_qasm)}")
        return { "circuit": circuit_qasm }

    @scheduling_qaoa_circuit_refinement_worker.task(task_type = "scheduling_qaoa_circuit-refinement")
    async def qaoa_circuit_refinement(job: Job):
        print(f"Scheduling circuit refinement started: {job.process_instance_key}")

        circuit = qiskit.qasm3.loads(job.variables["circuit"])
        problem_graph = json_graph.node_link_graph(job.variables["problemGraph"], edges = "edges")

        enriched_circuit = refine_circuit_maxcut(circuit, problem_graph)
        enriched_circuit_qasm = qiskit.qasm2.dumps(enriched_circuit)

        print(f"Scheduling circuit refinement finished: {len(enriched_circuit_qasm)}")
        return { "circuitEnriched": enriched_circuit_qasm }

    # QAOA for knapsack

    @knapsack_qaoa_circuit_generation_worker.task(task_type = "knapsack_qaoa_circuit-generation")
    async def qaoa_circuit_generation(job: Job):
        print(f"Knapsack circuit generation started: {job.process_instance_key}")

        values = job.variables["values"]
        weights = job.variables["weights"]
        capacity = job.variables["capacity"]
        circuit = generate_circuit_knapsack(values, weights, capacity)
        circuit_qasm = qiskit.qasm3.dumps(circuit)

        print(f"Knapsack circuit generation finished: {len(circuit_qasm)}")
        return { "circuit": circuit_qasm }

    @knapsack_qaoa_circuit_refinement_worker.task(task_type = "knapsack_qaoa_circuit-refinement")
    async def qaoa_circuit_refinement(job: Job):
        print(f"Knapsack circuit refinement started: {job.process_instance_key}")

        circuit = qiskit.qasm3.loads(job.variables["circuit"])
        values = job.variables["values"]
        weights = job.variables["weights"]
        capacity = job.variables["capacity"]

        enriched_circuit = refine_circuit_knapsack(circuit, values, weights, capacity)
        enriched_circuit_qasm = qiskit.qasm2.dumps(enriched_circuit)

        print(f"Knapsack circuit refinement finished: {len(enriched_circuit_qasm)}")
        return { "circuitEnriched": enriched_circuit_qasm }

    # Algorithm-independent

    @device_selection_worker.task(task_type = "quantum_device-selection")
    async def device_selection(job: Job):
        print(f"Device selection started: {job.process_instance_key}")

        selected_device = select_device()

        print(f"Device selection finished: {selected_device["key"]}")
        return { "selectedDevice": selected_device }

    @circuit_execution_worker.task(task_type = "quantum_circuit-execution")
    async def circuit_execution(job: Job):
        print(f"Circuit execution started: {job.process_instance_key}")

        enriched_circuit = qiskit.qasm2.loads(job.variables["circuitEnriched"], custom_instructions = qiskit.qasm2.LEGACY_CUSTOM_INSTRUCTIONS)
        device_configuration = job.variables["selectedDevice"]

        circuit_execution_result = execute_circuit(enriched_circuit, device_configuration)
        most_frequent_state = dict(sorted(circuit_execution_result["states"].items(), key = lambda key_val: key_val[1], reverse = True)[:1])

        print(f"Circuit execution finished after {circuit_execution_result["shots"]}: {most_frequent_state}")
        return { "circuitExecutionResult": circuit_execution_result }

    await asyncio.gather(
        scheduling_qaoa_circuit_generation_worker.work(),
        scheduling_qaoa_circuit_refinement_worker.work(),
        knapsack_qaoa_circuit_generation_worker.work(),
        knapsack_qaoa_circuit_refinement_worker.work(),
        circuit_execution_worker.work(),
        device_selection_worker.work()
    )

asyncio.run(main())
