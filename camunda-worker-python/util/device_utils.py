import json


def export_configuration(device: dict, path: str) -> str:
    device_json = json.dumps(device)
    with open(path, "w", encoding = "utf8") as file:
        file.write(device_json)
    return device_json

def import_configuration(path: str) -> dict:
    with open(path, "r", encoding = "utf8") as file:
        return json.loads(file.read())
