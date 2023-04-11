package me.opkarol.jobs.inventories;

import me.opkarol.OpJobs;
import me.opkarol.jobs.JobAction;
import me.opkarol.jobs.database.ActiveJobsDatabase;
import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.holder.IInventoryHolder;
import me.opkarol.opc.api.gui.inventory.InventoryFactory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.utils.StringUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobProgressInventory implements IInventoryHolder {
    private final OpInventory inventory;

    public JobProgressInventory(@NotNull Player player) {
        this.inventory = new OpInventory(new InventoryFactory(45, "#<5389FD>&lPostęp"));

        ActiveJobsDatabase ACTIVE_JOBS_DATABASE = OpJobs.getInstance().getActiveJobsDatabase();
        ACTIVE_JOBS_DATABASE.getMap().getByKey(player.getUniqueId()).ifPresent(job -> {
            @NotNull List<JobAction> jobActions = job.getJobActions();
            List<InventoryItem> inventoryItems = new ArrayList<>();
            for (JobAction jobAction : jobActions) {
                String objective = String.valueOf(jobAction.getObjective());
                InventoryItem item = new InventoryItem(StringUtil.getMaterialFromString(objective), event -> event.setCancelled(true));
                                        item.setName("#<5389FD>&l" + objective);
                        item.setLore(Arrays.asList("&7Do zdobycia: #<5389FD>&l" + jobAction.getObjectiveAmount(),
                                "&7Zebrano: #<5389FD>&l" + jobAction.getObjectivesCompleted(),
                                "&7Ukończone: " + (jobAction.isActionCompleted() ? "&a&lTAK" : "&c&lNIE")));
                        item.setFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

                if (jobAction.isActionCompleted()) {
                    item.setEnchants(Tuple.of(Enchantment.LUCK, 1));
                }
                inventoryItems.add(item);
            }
            BlockLayout27.setInventoryBlocks(inventory, inventoryItems, 9);
        });

        inventory.setAllUnused(0, getBlankItem());
        inventory.build();
    }

    @Override
    public OpInventory getInventory() {
        return inventory;
    }
}
