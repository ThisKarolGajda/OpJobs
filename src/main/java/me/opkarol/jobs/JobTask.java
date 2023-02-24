package me.opkarol.jobs;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

import java.io.Serializable;
import java.util.List;

public enum JobTask implements Serializable {
    MINE(BlockBreakEvent.class),
    HARVEST(BlockBreakEvent.class, PlayerHarvestBlockEvent.class),
    CRAFT(CraftItemEvent.class),
    SMELT(FurnaceExtractEvent.class),
    FISH(PlayerFishEvent.class);

    private final List<Class<? extends Event>> classes;

    @SafeVarargs
    JobTask(Class<? extends Event>... classes) {
        this.classes = List.of(classes);
    }

    public List<Class<? extends Event>> getClasses() {
        return classes;
    }
}
