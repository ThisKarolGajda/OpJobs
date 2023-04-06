package me.opkarol.jobs.factories;

import me.opkarol.jobs.JobAction;

public class ActionFactory extends JobAction {
    private final Object objective;
    private final int objectiveAmount;

    public ActionFactory(Object objective, int objectiveAmount) {
        this.objective = objective;
        this.objectiveAmount = objectiveAmount;
    }

    @Override
    public Object getObjective() {
        return objective;
    }

    @Override
    public int getObjectiveAmount() {
        return objectiveAmount;
    }
}
