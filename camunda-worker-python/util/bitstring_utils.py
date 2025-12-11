import json


def export_execution_result(result: dict, path: str):
    with open(path, 'w') as file:
        json.dump(result, file)
