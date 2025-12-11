from uuid import uuid4

from dto import Agent


class Shift:
    id: str
    start: int
    end: int
    type: str
    break_hours: float
    agents: list[Agent] = []
    needed_agents: int

    def __init__(self, _id: str = None):
        self.id = _id if _id is not None else uuid4().__str__()

    def copy(self):
        return Shift.from_dict(self.to_dict(), False)

    def to_dict(self):
        return {
            "id": self.id,
            "start": self.start,
            "end": self.end,
            "type": self.type,
            "break_hours": self.break_hours,
            "needed_agents": self.needed_agents,
            "agents": [agent.to_dict() for agent in self.agents],
        }

    @staticmethod
    def from_dict(d: dict, copy_id: bool = True):
        shift = Shift(d["id"] if copy_id else None)

        for key, value in d.items():
            if key == "id":
                continue
            shift.__setattr__(key, value)

        shift.agents = [Agent.from_dict(child) for child in d["agents"]]
        return shift
