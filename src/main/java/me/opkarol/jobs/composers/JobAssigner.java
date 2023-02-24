package me.opkarol.jobs.composers;

import me.opkarol.OpJobs;
import me.opkarol.effects.ParticleManager;
import me.opkarol.experience.Experience;
import me.opkarol.experience.ExperienceDatabase;
import me.opkarol.jobs.Job;
import me.opkarol.jobs.JobAction;
import me.opkarol.jobs.database.ActiveJobsDatabase;
import me.opkarol.opc.api.wrappers.OpActionBar;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class JobAssigner {
    private static final ActiveJobsDatabase activeJobs = ActiveJobsDatabase.getInstance();
    private static final ExperienceDatabase experienceDatabase = ExperienceDatabase.getInstance();
    private static final String ADVANCE_IN_OBJECTIVE_ACTIONBAR_MESSAGE = OpJobs.getValue("ADVANCE_IN_OBJECTIVE_ACTIONBAR_MESSAGE");
    private static final String COMPLETE_JOB_ACTIONBAR_MESSAGE = OpJobs.getValue("COMPLETE_JOB_ACTIONBAR_MESSAGE");
    private static final OpActionBar EMPTY_ACTION_BAR = new OpActionBar("null");

    public static ASSIGN_RESULT assignJobToPlayer(Player player, Job job) {
        UUID uuid = player.getUniqueId();

        Optional<Job> optional = activeJobs.getMap().getByKey(uuid);
        if (optional.isPresent()) {
            return ASSIGN_RESULT.ALREADY_HAS_JOB;
        }

        activeJobs.getMap().set(uuid, job);
        return ASSIGN_RESULT.SUCCESSFUL;
    }

    public static boolean completePlayerJob(Player player) {
        UUID uuid = player.getUniqueId();

        Optional<Job> optional = activeJobs.getMap().getByKey(uuid);
        if (optional.isEmpty()) {
            return false;
        }

        Job job = optional.get();
        activeJobs.getMap().remove(uuid, job);

        // Experience
        Experience experience = experienceDatabase.getExperience(uuid);
        experience.addExperience(job.getPoints());
        experienceDatabase.getMap().set(uuid, experience);

        // Effects
        EMPTY_ACTION_BAR.setText(COMPLETE_JOB_ACTIONBAR_MESSAGE)
                .build()
                .send(player);
        ParticleManager.particleCircleSpellEffect(player);


        //todo add rewards
        return true;
    }


    public static void advanceInObjective(Player player, JobAction jobAction) {
        EMPTY_ACTION_BAR.setText(ADVANCE_IN_OBJECTIVE_ACTIONBAR_MESSAGE
                        .replace("%block_type%", jobAction.getObjective().toString())
                        .replace("%amount%", String.valueOf(jobAction.getObjectivesCompleted()))
                        .replace("%amount_left%", String.valueOf(jobAction.getObjectiveAmount())))
                .build()
                .send(player);
    }

    enum ASSIGN_RESULT {
        ALREADY_HAS_JOB,
        SUCCESSFUL,
    }
}
