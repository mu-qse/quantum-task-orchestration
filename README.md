# A Reference Architecture for Embedding Quantum Software Into Enterprise Systems

Proceedings paper: [10.1145/3748522.3779937](https://doi.org/10.1145/3748522.3779937) (3 pages)

Manuscript submitted to ACM: [10.48550/arXiv.2512.12009](https://doi.org/10.48550/arXiv.2512.12009) (10 pages)

Authors: Marc Uphues, Sebastian Thöne, and Herbert Kuchen

# Case Studies
This repository contains an implementation of our proposed reference architecture.
The implementation is utilized in two case studies exemplifying our proposals.
In each of these case studies, we implement a typical enterprise system, which embeds quantum software for solving a common combinatorial challenge from the field of operations research.

### Call Center Agent Scheduling (Nurse Scheduling Problem)
As a first case study, we selected the well-known Nurse Scheduling Problem (NSP).
It involves assigning employees to shifts in a way that satisfies multiple constraints.
To show the practical usability of our solution, we adapted this to a typical call center environment, where employees are also called "agents".

### Cargo Ship Container Composition (Knapsack Problem)
As a second case study, we chose the knapsack problem.
It comprises the selection of items to maximize the total value of these selected items, while ensuring that their combined weight does not exceed a given capacity.
We adapted this to a common logistic scenario in which a cargo ship must be loaded with various containers, each differing in weight and value.
Given the ship's limited capacity, the goal is to determine an optimal composition that maximizes the total value without exceeding the capacity constraint.

## Requirements

You can either use a [local Camunda 8.6 instance](https://docs.camunda.io/docs/8.6/self-managed/setup/deploy/local/c8run/), or an already deployed Camunda instance on a remote host.

For the worker builds, you will need:
- IntelliJ IDEA
- Java JDK 21
- Python 3.12

## Build of the Camunda Worker in Java

1. Build the [Camunda Worker in Java](camunda-worker-java) using the build tool Maven.
2. Start the built Camunda Worker in Java (Main class is `de.fhms.mu.CamundaWorkerApplication`).

## Build of the Camunda Worker in Python

1. Prepare a Python virtual environment within [Camunda Worker in Python](camunda-worker-python): `python -m venv .venv` (Windows) or `python3 -m venv .venv` (Linux)

2. Activate the created virtual environment: `.venv\Scripts\activate.bat` (Windows) or `source .venv/bin/activate` (Linux)

3. Install the necessary dependencies within the virtual environment: `pip install -r requirements.txt`

4. Start the Camunda Worker in Python within the virtual environment: `python zeebe-worker.py` (Windows) or `python3 zeebe-worker.py` (Linux)

## Deployment and Run

1. Deploy the provided BPMN models (.bpmn) and Input Forms (.form) from the [diagrams](diagrams) directory to your Camunda instance using the Camunda Modeler.
2. Deploy the provided  from the [diagrams](diagrams) directory to your Camunda instance using the Camunda Modeler.
3. Run either the `callcenter-scheduling.bpmn` or the `cargoship-container-composition.bpmn` model via the Camunda Modeler.
4. Go to Camunda Tasklist, assign the Input Task to yourself, and provide the necessary inputs to the form. To use the QAOA strategy, use `qaoa` for the `preferredStrategyKey` field.