package me.opkarol.jobs;

import me.opkarol.jobs.composers.JobAssigner;
import me.opkarol.jobs.database.ActiveJobsDatabase;
import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.event.EventRegister;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
                OpAPI.logInfo(Arrays.toString(event.getBlock().getDrops(player.getInventory().getItemInMainHand()).toArray()));
                OpAPI.logInfo(action.getObjective().toString());
                OpAPI.logInfo(event.getBlock().getType().toString());

                String fixedType = switch (event.getBlock().getType()) {
                    case SWEET_BERRY_BUSH -> "SWEET_BERRIES";
                    default -> event.getBlock().getType().toString();
                };

                // Check if the mined block is the same as objective && if there is a drop from the block
                if (!(event.getBlock().getDrops(player.getInventory().getItemInMainHand()).stream()
                        .map(itemStack -> itemStack.getType().toString()).toList().size() > 0) ||
                        !action.getObjective().toString().equals(fixedType)) {
                    continue;
                }

                // Remove broke block and add point to objective
                action.incrementObjectiveAmount(event.getBlock().getDrops().stream().mapToInt(ItemStack::getAmount).sum());
                if (action.isActionCompleted()) {
                    JobAssigner.informCompletingAction(player, action, job.getProfile().event().displayName());
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

        EventRegister.registerEvent(PlayerHarvestBlockEvent.class, EventPriority.HIGH, event -> {
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
                List<ItemStack> itemsHarvested = event.getItemsHarvested();
                if (!(itemsHarvested.stream()
                        .map(ItemStack::getType)
                        .map(Objects::toString).toList()
                        .contains(action.getObjective().toString()))) {
                    continue;
                }

                // Remove broke block and add point to objective
                action.incrementObjectiveAmount(event.getItemsHarvested().size());
                if (action.isActionCompleted()) {
                    JobAssigner.informCompletingAction(player, action, job.getProfile().event().displayName());
                }
                event.getItemsHarvested().clear();
                completedAnyAction = true;
                JobAssigner.informAdvancingAction(player, action);
            }
            // Check if job is completed
            if (completedAnyAction) {
                job.tryToCompleteJob(player);
            }
        });

        EventRegister.registerEvent(CraftItemEvent.class, EventPriority.HIGH, event -> {
            if (event.isCancelled()) {
                return;
            }


            Player player = (Player) event.getWhoClicked();
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
                if (!action.getObjective().toString().equals(event.getCurrentItem().getType().toString())) {
                    continue;
                }

                // Remove broke block and add point to objective
                action.incrementObjectiveAmount(event.getCurrentItem().getAmount());
                if (action.isActionCompleted()) {
                    JobAssigner.informCompletingAction(player, action, job.getProfile().event().displayName());
                }

                // Fixes bug where you have multiple results, and it only counts one of it
                event.setCancelled(true);
                event.setCurrentItem(null);

                ItemStack[] matrix = event.getInventory().getMatrix();
                for (int i = 0; i < matrix.length; i++) {
                    ItemStack item = matrix[i];
                    if (item != null && item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        matrix[i] = null;
                    }
                }
                event.getInventory().setMatrix(matrix);

                completedAnyAction = true;
                JobAssigner.informAdvancingAction(player, action);
            }
            // Check if job is completed
            if (completedAnyAction) {
                job.tryToCompleteJob(player);
            }
        });

        EventRegister.registerEvent(InventoryClickEvent.class, EventPriority.HIGH, event -> {
            if (!(event.getClickedInventory() instanceof FurnaceInventory) || event.getSlot() != 2) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
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
                if (!action.getObjective().toString().equals(event.getCurrentItem().getType().toString())) {
                    continue;
                }
                int amount = event.getCurrentItem().getAmount();

                // Remove broke block and add point to objective
                action.incrementObjectiveAmount(amount);
                if (action.isActionCompleted()) {
                    JobAssigner.informCompletingAction(player, action, job.getProfile().event().displayName());
                }
                event.setCancelled(true);
                event.setCurrentItem(null);

                completedAnyAction = true;
                JobAssigner.informAdvancingAction(player, action);
            }
            // Check if job is completed
            if (completedAnyAction) {
                job.tryToCompleteJob(player);
            }
        });

        EventRegister.registerEvent(PlayerFishEvent.class, EventPriority.HIGH, event -> {
            if (event.isCancelled() || event.getCaught() == null) {
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
                OpAPI.logInfo(action.getObjective().toString());
                OpAPI.logInfo(((Item) event.getCaught()).getItemStack().getType().toString());

                // Check if the mined block is the same as objective && if there is a drop from the block
                if (!action.getObjective().toString().equals(((Item) event.getCaught()).getItemStack().getType().toString())) {
                    continue;
                }

                action.incrementObjectiveAmount();
                if (action.isActionCompleted()) {
                    JobAssigner.informCompletingAction(player, action, job.getProfile().event().displayName());
                }

                event.setCancelled(true);
                event.setExpToDrop(0);

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