package me.opkarol.jobs;

import me.opkarol.jobs.composers.JobAssigner;
import me.opkarol.jobs.database.ActiveJobsDatabase;
import me.opkarol.opc.api.event.EventRegister;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;
import java.util.UUID;

public class JobListener {

    public JobListener(ActiveJobsDatabase jobsDatabase) {
        EventRegister.registerEvent(BlockBreakEvent.class, EventPriority.HIGH, event -> {
            if (event.isCancelled()) {
                return;
            }

            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            Optional<Job> optional = jobsDatabase.getMap().getByKey(uuid);
            if (optional.isEmpty()) {
                // Player doesn't currently have a job
                return;
            }

            boolean completedAnyAction = false;
            Job job = optional.get();
            for (JobAction action : job.getJobActions()) {
                if (action.isActionCompleted() || job.getProfile().event() == null) {
                    continue;
                }

                // Check if the action is the same as the event
                if (!job.getProfile().event().events().contains(event.getClass().toString())) {
                    continue;
                }

                // Check if the mined block is the same as objective && if there is a drop from the block
                if (!(event.getBlock().getDrops(player.getInventory().getItemInMainHand()).stream()
                        .map(itemStack -> itemStack.getType().toString()).toList().size() > 0) ||
                        !action.getObjective().toString().equals(event.getBlock().getType().toString())) {
                    continue;
                }

                // Remove broke block and add point to objective
                action.incrementObjectiveAmount();
                if (action.isActionCompleted()) {
                    JobAssigner.informCompletingAction(player, action);
                }
                event.setDropItems(false);
                completedAnyAction = true;
                JobAssigner.informAdvancingAction(player, action);
            }
            // Check if job is completed
            if (completedAnyAction) {
                job.tryToCompleteJob(player);
            }
        });
    }

}
