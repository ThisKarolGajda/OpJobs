package me.opkarol.jobs.inventories;

import me.opkarol.OpJobs;
import me.opkarol.jobs.Job;
import me.opkarol.jobs.JobProfile;
import me.opkarol.jobs.composers.JobAssigner;
import me.opkarol.jobs.composers.JobCreator;
import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.holder.InventoryHolder;
import me.opkarol.opc.api.gui.items.InventoryItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JobChooseInventory extends InventoryHolder {
    public JobChooseInventory() {
        super(27, "#<5389FD>&lWybierz pracę");
    }

    @Override
    public void setupInventory(OpInventory inventory) {
        OpJobs.getInstance().getInventoriesHolder().addInventory(getClass().getSimpleName(), this);

        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (JobProfile profile : OpJobs.getInstance().getJobProfilesDatabase().getProfiles()) {
            InventoryItem item = new InventoryItem(profile.headIcon(),
                    event -> {
                        event.setCancelled(true);
                        event.close();
                        Player player = (Player) event.getPlayer();
                        Job job = JobCreator.createRandomJobForPlayerWithSpecificProfile(player, profile.job());
                        JobAssigner.assignJobToPlayer(player, job);
                    });
            item.setName("#<5389FD>&l" + profile.displayName());
            item.setLore(getFormattedLore(profile));
            item.setFlags(ItemFlag.HIDE_ATTRIBUTES);
            inventoryItems.add(item);
        }

        BlocksLayout.setInventoryBlocks(inventory, inventoryItems);
        inventory.setAllUnused(0, getBlankItem());
        inventory.build();
    }

    public List<String> getFormattedLore(@NotNull JobProfile profile) {
        List<String> temp = new ArrayList<>();
        temp.add("&7Zadania:" + (profile.event().displayName() == null ? "" : " &8[&7" + profile.event().displayName() + "&8]"));
        profile.objectives().forEach(jobObjective ->
                temp.add("&7- #<5389FD>" + jobObjective.getFixedName() + " &8[&7" + jobObjective.getPoints() + " &7pkt&8]"));
        return temp;
    }

}