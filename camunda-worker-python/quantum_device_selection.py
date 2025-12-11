import sys
import os

from util import device_utils

def select_device() -> dict:
    return {
        "key": "default",
        "shots": 10000,
    }

if __name__ == '__main__':
    # Get parameters from cli args
    temp_dir_path = sys.argv[1] if len(sys.argv) > 1 else "./"
    selected_device_path = os.path.join(temp_dir_path, "selected-device.json")

    # Device selection
    selected_device = select_device()

    # Device configuration export
    device_utils.export_configuration(selected_device, selected_device_path)

