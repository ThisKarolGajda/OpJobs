package me.opkarol.jobs.inventories;

import me.opkarol.OpJobs;
import me.opkarol.experience.ExperienceDatabase;
import me.opkarol.experience.ExperienceProfile;
import me.opkarol.jobs.JobProfile;
import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.holder.InventoriesHolder;
import me.opkarol.opc.api.gui.holder.InventoryHolder;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.map.OpMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobExperienceInventory extends InventoryHolder {
    public JobExperienceInventory() {
        super(27, "#<5389FD>&lPoziomy prac");
    }

    @Override
    public void setupInventory(OpInventory inventory) {
        OpJobs.getInstance().getInventoriesHolder().addInventory(getClass().getSimpleName(), this);

        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (JobProfile profile : OpJobs.getInstance().getJobProfilesDatabase().getProfiles()) {
            InventoryItem item = new InventoryItem(profile.headIcon(),
                    event -> event.setCancelled(true));

            item.setName("#<5389FD>&l" + profile.displayName());
            item.setLore(getFixedLore(profile.job()));
            item.setFlags(ItemFlag.HIDE_ATTRIBUTES);
            inventoryItems.add(item);

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.getInventory().addItem(profile.headIcon());
            });
        }

        BlocksLayout.setInventoryBlocks(inventory, inventoryItems);
        inventory.setAllUnused(0, getBlankItem());
        inventory.setAutoBuild(false);
    }

    public List<String> getFixedLore(@NotNull String job) {
        List<String> list = Arrays.asList("&7Poziom: #<5389FD>%%job%_level%",
                "&7Postęp: #<5389FD>%%job%_experience%&7/#<5389FD>%%job%_current_level% &8(#<5389FD>%%job%_current_level_percentage%&8)");
        list.replaceAll(s -> s.replace("%job%", job.toLowerCase()));
        return list;
    }

    public static void openExperienceInventory(@NotNull Player player, @NotNull InventoriesHolder inventoriesHolder) {
        ExperienceDatabase experienceDatabase = OpJobs.getInstance().getExperienceDatabase();
        OpMap<String, String> replacements = new OpMap<>();
        ExperienceProfile experienceProfile = experienceDatabase.getProfile(player.getUniqueId());
        experienceProfile.getJobsWithExperience().getMap().forEach((job, experience) -> {
            String prefix = "%" + job.toLowerCase() + "_&change%";
            replacements.set(prefix.replace("&change", "experience"), String.valueOf(experience.getExperience()));
            replacements.set(prefix.replace("&change", "level"), String.valueOf(experience.getLevel()));
            replacements.set(prefix.replace("&change", "current_level"), String.valueOf(experience.calculateExperiencePointsForCurrentLevel()));
            replacements.set(prefix.replace("&change", "current_level_percentage"), experience.getPercentage());
        });

        inventoriesHolder.getInventory("JobExperienceInventory")
                .ifPresent(holder -> {
                    OpAPI.logInfo(holder.getInventory().getInventoryHolder().isBuilt(0));
                    holder.getInventory().open(player, replacements);
                    OpAPI.logInfo(holder.getInventory().getInventoryHolder().isBuilt(0));
                });
    }
}
