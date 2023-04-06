package me.opkarol.jobs.composers;

import me.opkarol.OpJobs;
import me.opkarol.experience.Experience;
import me.opkarol.experience.ExperienceDatabase;
import me.opkarol.jobs.Job;
import me.opkarol.jobs.JobAction;
import me.opkarol.jobs.JobObjective;
import me.opkarol.jobs.JobProfile;
import me.opkarol.jobs.database.JobProfilesDatabase;
import me.opkarol.jobs.factories.ActionFactory;
import me.opkarol.jobs.factories.JobFactory;
import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.misc.Tuple;
import me.opkarol.opc.api.utils.MathUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class JobCreator {
    private static final Random RANDOM = new Random();
    private static final JobProfilesDatabase JOB_PROFILES_DATABASE = OpJobs.getInstance().getJobProfilesDatabase();
    private static final ExperienceDatabase EXPERIENCE_DATABASE = OpJobs.getInstance().getExperienceDatabase();

    private final static int MAX_BLOCK_TYPES = OpAPI.getConfig().getInt("MAX_BLOCK_TYPES");

    public static String chooseRandomJobProfile() {
        OpList<JobProfile> profiles = JOB_PROFILES_DATABASE.getProfiles();
        int profilesSize = profiles.size();
        if (profilesSize == 0) {
            throw new RuntimeException("Profiles database list is empty.");
        }

        int random = MathUtils.getRandomInt(0, profilesSize - 1);
        return profiles.unsafeGet(random).job();
    }

    public static Job createRandomJobForPlayerWithSpecificProfile(Player player) {
        return createRandomJobForPlayerWithSpecificProfile(player, chooseRandomJobProfile());
    }

    public static Job createRandomJobForPlayerWithSpecificProfile(Player player, String job) {
        Optional<JobProfile> optional = JOB_PROFILES_DATABASE.getProfile(job);
        if (optional.isEmpty()) {
            return null;
        }

        List<JobObjective> jobObjectives = optional.get().objectives();
        UUID uuid = player.getUniqueId();
        Experience experience = EXPERIENCE_DATABASE.getExperience(uuid, job);
        Tuple<Map<String, Integer>, Integer> selectedObjectives = calculateRandomObjectives(jobObjectives, experience);

        List<JobAction> jobActionList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : selectedObjectives.first().entrySet()) {
            JobAction action = new ActionFactory(entry.getKey(), entry.getValue());
            jobActionList.add(action);
        }

        return new JobFactory(job, jobActionList, selectedObjectives.second());
    }

    @NotNull
    private static Tuple<Map<String, Integer>, Integer> calculateRandomObjectives(List<JobObjective> jobObjectives, Experience experience) {
        int maxPoints = experience.getLevel() * 30;
        Map<String, Integer> selectedObjectives = new HashMap<>();
        int currentCost = 0;

        List<String> randomObjectives = jobObjectives.stream().map(JobObjective::getName).collect(Collectors.toList());
        Collections.shuffle(randomObjectives);
        randomObjectives = randomObjectives.size() > MAX_BLOCK_TYPES ? randomObjectives.subList(0, MAX_BLOCK_TYPES) : randomObjectives;
        int notSet = 0;
        while (currentCost < maxPoints && notSet < 51) {
            Collections.shuffle(randomObjectives);
            String objective = randomObjectives.get(RANDOM.nextInt(randomObjectives.size()));
            Optional<JobObjective> optional = jobObjectives.stream().filter(jobObjective -> jobObjective.getName().equals(objective)).findFirst();
            if (optional.isEmpty()) {
               break;
            }
            int objectiveCost = optional.get().getPoints();
            if (currentCost + objectiveCost <= maxPoints) {
                selectedObjectives.put(objective, selectedObjectives.getOrDefault(objective, 0) + 1);
                currentCost += objectiveCost;
            } else {
                notSet++;
            }
        }
        return Tuple.of(selectedObjectives, currentCost);
    }

}