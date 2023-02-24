package me.opkarol.commands;

import me.opkarol.jobs.Job;
import me.opkarol.jobs.JobTask;
import me.opkarol.jobs.composers.JobAssigner;
import me.opkarol.jobs.composers.JobCreator;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

@Command("praca")
public class JobCommand {

    @DefaultFor("praca")
    public void jobMain(Player player) {
        // open main gui
    }

    @Subcommand("szukac")
    public void jobSearch(Player player) {
        // open job search gui
    }

    @Subcommand("test")
    public void randomJobTest(Player player) {
        Job job = JobCreator.createRandomJobForPlayer(player, JobTask.MINE);
        JobAssigner.assignJobToPlayer(player, job);
    }
}
