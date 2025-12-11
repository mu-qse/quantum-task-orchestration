import datetime


class Employee:
    id: str
    first_name: str
    last_name: str
    birth_date: datetime

    def __init__(self):
        pass

    def to_dict(self):
        return {
            "id": self.id,
            "first_name": self.first_name,
            "last_name": self.last_name,
            "birth_date": self.birth_date,
        }
