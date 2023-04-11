package me.opkarol.commands;

import me.opkarol.OpJobs;
import me.opkarol.jobs.Job;
import me.opkarol.jobs.composers.JobAssigner;
import me.opkarol.jobs.composers.JobCreator;
import me.opkarol.jobs.database.ActiveJobsDatabase;
import me.opkarol.jobs.inventories.JobProgressInventory;
import me.opkarol.jobs.inventories.JobMainInventory;
import me.opkarol.opc.api.gui.holder.InventoriesHolder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

import static me.opkarol.jobs.inventories.JobExperienceInventory.openExperienceInventory;

@Command("praca")
public class JobCommand {
    private final InventoriesHolder inventoriesHolder;
    private final ActiveJobsDatabase activeJobsDatabase = OpJobs.getInstance().getActiveJobsDatabase();

    public JobCommand(InventoriesHolder inventoriesHolder) {
        this.inventoriesHolder = inventoriesHolder;
    }

    @DefaultFor("praca")
    public void jobMain(Player player) {
        new JobMainInventory(player);
    }

    @Subcommand("wybierz")
    public void jobSearch(Player player) {
        inventoriesHolder.getInventory("JobChooseInventory").ifPresent(holder -> holder.getInventory().openInventory(player));
    }

    @Subcommand("poziomy")
    public void jobExperienceLevels(@NotNull Player player) {
        openExperienceInventory(player, inventoriesHolder);
    }

    @Subcommand("losowa")
    public void randomJobTest(Player player) {
        Job job = JobCreator.createRandomJob(player);
        JobAssigner.assignJobToPlayer(player, job);
    }

    @Subcommand("postep")
    public void jobProgress(@NotNull Player player) {
        if (!activeJobsDatabase.getMap().containsKey(player.getUniqueId())) {
            OpJobs.getInstance().sendMappedMessage(player, "PLAYER_NOT_HAVE_JOB");
            return;
        }
        new JobProgressInventory(player)
                .getInventory().openInventory(player);
    }
}
