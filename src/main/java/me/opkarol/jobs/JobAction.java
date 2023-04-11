package me.opkarol.jobs;

import java.io.Serializable;

public abstract class JobAction implements Serializable {
    private int objectivesCompleted;

    public abstract Object getObjective();

    public abstract int getObjectiveAmount();

    public void incrementObjectiveAmount() {
        objectivesCompleted++;
    }

    public void incrementObjectiveAmount(int times) {
        objectivesCompleted += times;
    }

    public boolean isActionCompleted() {
        return objectivesCompleted >= getObjectiveAmount();
    }

    public int getObjectivesCompleted() {
        return objectivesCompleted;
    }
}
