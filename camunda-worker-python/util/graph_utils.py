import json

import networkx as nx
from networkx.readwrite import json_graph
import rustworkx as rx


def import_graph(path: str) -> nx.Graph:
    with open(path, 'r') as file:
        input_json = json.load(file)
    return json_graph.node_link_graph(input_json, edges = "edges")

def convert_rx_to_nx(graph: rx.PyGraph) -> nx.Graph:
    edges = [
        (
            graph[x[0]],
            graph[x[1]],
            {'weight': x[2]}
        ) for x in graph.weighted_edge_list()
    ]

    if isinstance(graph, rx.PyGraph):
        if graph.multigraph:
            return nx.MultiGraph(edges)
        else:
            return nx.Graph(edges)
    else:
        if graph.multigraph:
            return nx.MultiDiGraph(edges)
        else:
            return nx.DiGraph(edges)

def convert_nx_to_rx(graph: nx.Graph) -> rx.PyGraph:
    return rx.networkx_converter(graph)
