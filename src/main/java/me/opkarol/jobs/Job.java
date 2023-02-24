package me.opkarol.jobs;

import me.opkarol.jobs.composers.JobAssigner;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public interface Job extends Serializable {

    JobTask getType();

    @NotNull
    List<JobAction> getJobActions();

    int getPoints();

    default boolean hasCompletedAllActions() {
        for (JobAction jobAction : getJobActions()) {
            if (!jobAction.isActionCompleted()) {
                return false;
            }
        }
        return true;
    }

    default void tryToCompleteJob(Player player) {
        if (hasCompletedAllActions()) {
            JobAssigner.completePlayerJob(player);
        }
    }
}
