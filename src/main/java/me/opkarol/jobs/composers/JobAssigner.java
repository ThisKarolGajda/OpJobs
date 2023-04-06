package me.opkarol.jobs.composers;

import me.opkarol.OpJobs;
import me.opkarol.effects.ParticleManager;
import me.opkarol.experience.Experience;
import me.opkarol.experience.ExperienceDatabase;
import me.opkarol.jobs.Job;
import me.opkarol.jobs.JobAction;
import me.opkarol.jobs.database.ActiveJobsDatabase;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.wrappers.OpBossBar;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class JobAssigner {
    public static final OpJobs INSTANCE = OpJobs.getInstance();
    private static final ActiveJobsDatabase ACTIVE_JOBS_DATABASE = INSTANCE.getActiveJobsDatabase();
    private static final ExperienceDatabase EXPERIENCE_DATABASE = INSTANCE.getExperienceDatabase();
    private static final String ADVANCED_IN_OBJECTIVE = INSTANCE.getValue("ADVANCED_IN_OBJECTIVE");
    private static final String ASSIGNED_NEW_JOB = INSTANCE.getFormattedValue("ASSIGNED_NEW_JOB");
    private static final String COMPLETED_JOB = INSTANCE.getValue("COMPLETED_JOB");
    private static final String COMPLETED_OBJECTIVE = INSTANCE.getFormattedValue("COMPLETED_OBJECTIVE");
    private static final String ALREADY_HAS_JOB = INSTANCE.getFormattedValue("ALREADY_HAS_JOB");
    private static final OpMap<UUID, OpBossBar> ACTIVE_BOSS_BARS = new OpMap<>();

    public static ASSIGN_RESULT assignJobToPlayer(Player player, Job job) {
        UUID uuid = player.getUniqueId();

        Optional<Job> optional = ACTIVE_JOBS_DATABASE.getMap().getByKey(uuid);
        if (optional.isPresent()) {
            player.sendMessage(ALREADY_HAS_JOB);
            return ASSIGN_RESULT.ALREADY_HAS_JOB;
        }

        ACTIVE_JOBS_DATABASE.getMap().set(uuid, job);
        player.sendMessage(ASSIGNED_NEW_JOB);
        return ASSIGN_RESULT.SUCCESSFUL;
    }

    public static boolean completePlayerJob(Player player) {
        UUID uuid = player.getUniqueId();

        Optional<Job> optional = ACTIVE_JOBS_DATABASE.getMap().getByKey(uuid);
        if (optional.isEmpty()) {
            return false;
        }

        Job job = optional.get();
        ACTIVE_JOBS_DATABASE.getMap().remove(uuid, job);

        // Experience
        Experience experience = EXPERIENCE_DATABASE.getExperience(uuid, job.getJobName());
        experience.addExperience(job.getPoints());
        EXPERIENCE_DATABASE.setExperience(uuid, job.getJobName(), experience);

        // Effects
        OpBossBar bossBar;
        if (ACTIVE_BOSS_BARS.containsKey(uuid)) {
            bossBar = ACTIVE_BOSS_BARS.unsafeGet(uuid);
            bossBar.removeDisplay(player);
            bossBar.getRunnable().cancelTask();
        } else {
            bossBar = new OpBossBar();
        }
        bossBar.setTitle(COMPLETED_JOB);
        bossBar.display(player);
        bossBar.loopAndDisplay(5, 1, endBossBar -> ACTIVE_BOSS_BARS.remove(uuid, endBossBar));
        ACTIVE_BOSS_BARS.set(uuid, bossBar);

        ParticleManager.particleInCircleEffect(player, Particle.SPELL);

        //todo add rewards
        return true;
    }


    public static void informAdvancingAction(Player player, @NotNull JobAction jobAction) {
        UUID uuid = player.getUniqueId();
        OpBossBar bossBar;
        if (ACTIVE_BOSS_BARS.containsKey(uuid)) {
            bossBar = ACTIVE_BOSS_BARS.unsafeGet(uuid);
            bossBar.removeDisplay(player);
            bossBar.getRunnable().cancelTask();
        } else {
            bossBar = new OpBossBar();
        }
        bossBar.setTitle(ADVANCED_IN_OBJECTIVE
                .replace("%block_type%", jobAction.getObjective().toString())
                .replace("%amount%", String.valueOf(jobAction.getObjectivesCompleted()))
                .replace("%amount_left%", String.valueOf(jobAction.getObjectiveAmount())));
        bossBar.display(player);
        bossBar.loopAndDisplay(5, 1, endBossBar -> ACTIVE_BOSS_BARS.remove(uuid, endBossBar));
        ACTIVE_BOSS_BARS.set(uuid, bossBar);

    }

    public static void informCompletingAction(@NotNull Player player, @NotNull JobAction action) {
        player.sendMessage(COMPLETED_OBJECTIVE.replace("%block_type%", String.valueOf(action.getObjective())));
    }

    enum ASSIGN_RESULT {
        ALREADY_HAS_JOB,
        SUCCESSFUL,
    }

}
