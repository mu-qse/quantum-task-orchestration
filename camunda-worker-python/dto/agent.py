from dto import Employee


class Agent(Employee):
    working_time: float
    efficiency: float

    def __init__(self):
        pass

    def to_dict(self):
        return super().to_dict() | {
            "working_time": self.working_time,
            "efficiency": self.efficiency,
        }

    @staticmethod
    def from_dict(d: dict):
        agent = Agent()

        for key, value in d.items():
            agent.__setattr__(key, value)

        return agent
