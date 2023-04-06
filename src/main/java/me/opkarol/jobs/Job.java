package me.opkarol.jobs;

import me.opkarol.jobs.composers.JobAssigner;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public abstract class Job implements Serializable {

    public abstract String getJobName();

    @NotNull
    public abstract List<JobAction> getJobActions();

    public abstract int getPoints();

    boolean hasCompletedAllActions() {
        for (JobAction jobAction : getJobActions()) {
            if (!jobAction.isActionCompleted()) {
                return false;
            }
        }
        return true;
    }

    public void tryToCompleteJob(Player player) {
        if (hasCompletedAllActions()) {
            JobAssigner.completePlayerJob(player);
        }
    }

    public abstract JobProfile getProfile();
}
