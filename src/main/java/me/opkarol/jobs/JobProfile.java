package me.opkarol.jobs;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.List;

public record JobProfile(String job, String displayName, List<JobObjective> objectives, JobEvent event, ItemStack headIcon)
        implements Serializable {
}
