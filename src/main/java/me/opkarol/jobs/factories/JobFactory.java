package me.opkarol.jobs.factories;

import me.opkarol.jobs.Job;
import me.opkarol.jobs.JobAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JobFactory extends Job {
    private final String job;
    private final List<JobAction> jobActionList;
    private final int points;

    public JobFactory(String job, List<JobAction> jobActionList, int points) {
        this.job = job;
        this.jobActionList = jobActionList;
        this.points = points;
    }

    @Override
    public String getJobName() {
        return job;
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
