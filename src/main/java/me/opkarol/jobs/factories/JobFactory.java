package me.opkarol.jobs.factories;

import me.opkarol.OpJobs;
import me.opkarol.jobs.Job;
import me.opkarol.jobs.JobAction;
import me.opkarol.jobs.JobProfile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class JobFactory extends Job {
    private final String job;
    private final List<JobAction> jobActionList;
    private final int points;
    private final JobProfile jobProfile;

    public JobFactory(String job, List<JobAction> jobActionList, int points, JobProfile jobProfile) {
        this.job = job;
        this.jobActionList = jobActionList;
        this.points = points;
        this.jobProfile = jobProfile;
    }

    public JobFactory(String job, List<JobAction> jobActionList, int points) {
        this.job = job;
        this.jobActionList = jobActionList;
        this.points = points;
        Optional<JobProfile> optional = OpJobs.getInstance().getJobProfilesDatabase().getProfile(job);
        if (optional.isPresent()) {
            this.jobProfile = optional.get();
        } else {
            throw new RuntimeException("Couldn't get " + job + " within the current database.");
        }
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

    @Override
    public JobProfile getProfile() {
        return jobProfile;
    }
}
