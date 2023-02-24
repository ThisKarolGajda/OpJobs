package me.opkarol.jobs.factories;

import me.opkarol.jobs.JobAction;
import me.opkarol.jobs.JobTask;

public class ActionFactory extends JobAction {
    private final JobTask task;
    private final Object objective;
    private final int objectiveAmount;

    public ActionFactory(JobTask task, Object objective, int objectiveAmount) {
        this.task = task;
        this.objective = objective;
        this.objectiveAmount = objectiveAmount;
    }

    @Override
    public JobTask getJobTask() {
        return task;
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
