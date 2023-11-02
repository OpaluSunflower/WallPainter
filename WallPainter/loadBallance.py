import Task

class loadBallance:

    def __init__(self):
        self.openPorts = 0
        self.enabledUnits = 0
        self.tasksOnUnits = {}

    def createUnit(self):
        self.enabledUnits +=1
        self.tasksOnUnits[self.enabledUnits] = [Task(),0]
