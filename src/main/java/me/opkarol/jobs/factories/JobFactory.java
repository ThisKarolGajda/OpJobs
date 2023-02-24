package me.opkarol.jobs.factories;

import me.opkarol.jobs.Job;
import me.opkarol.jobs.JobAction;
import me.opkarol.jobs.JobTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JobFactory implements Job {
    private final JobTask jobType;
    private final List<JobAction> jobActionList;
    private final int points;

    public JobFactory(JobTask jobTask, List<JobAction> jobActionList, int points) {
        this.jobType = jobTask;
        this.jobActionList = jobActionList;
        this.points = points;
    }

    @Override
    public JobTask getType() {
        return jobType;
    }

    @Override
    public @NotNull List<JobAction> getJobActions() {
        return jobActionList;
    }

    @Override
    public int getPoints() {
        return points;
    }
}
