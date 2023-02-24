package me.opkarol.jobs.composers;

import me.opkarol.experience.Experience;
import me.opkarol.experience.ExperienceDatabase;
import me.opkarol.jobs.Job;
import me.opkarol.jobs.JobAction;
import me.opkarol.jobs.JobTask;
import me.opkarol.jobs.database.ObjectivesDatabase;
import me.opkarol.jobs.factories.ActionFactory;
import me.opkarol.jobs.factories.JobFactory;
import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.Tuple;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JobCreator {
    private static final Random random = new Random();
    private static final ObjectivesDatabase objectivesFile = ObjectivesDatabase.getInstance();

    private final static int MAX_BLOCK_TYPES = OpAPI.getConfig().getInt("MAX_BLOCK_TYPES");

    public static Job createRandomJobForPlayer(Player player, JobTask jobTask) {
        Optional<OpMap<String, Integer>> optional = objectivesFile.getObjectives().getByKey(jobTask);
        if (optional.isEmpty()) {
            return null;
        }

        OpMap<String, Integer> jobObjectives = optional.get();
        UUID uuid = player.getUniqueId();
        Experience experience = ExperienceDatabase.getInstance().getExperience(uuid);
        Tuple<Map<String, Integer>, Integer> selectedObjectives = calculateRandomObjectives(jobObjectives, experience);

        List<JobAction> jobActionList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : selectedObjectives.first().entrySet()) {
            JobAction action = new ActionFactory(jobTask, entry.getKey(), entry.getValue());
            jobActionList.add(action);
        }

        return new JobFactory(jobTask, jobActionList, selectedObjectives.second());
    }

    @NotNull
    private static Tuple<Map<String, Integer>, Integer> calculateRandomObjectives(OpMap<String, Integer> jobObjectives, Experience experience) {
        int maxPoints = experience.getLevel() * 30;
        Map<String, Integer> selectedObjectives = new HashMap<>();
        int currentCost = 0;

        List<String> randomObjectives = new ArrayList<>(jobObjectives.keySet());
        Collections.shuffle(randomObjectives);
        randomObjectives = randomObjectives.size() > MAX_BLOCK_TYPES ? randomObjectives.subList(0, MAX_BLOCK_TYPES) : randomObjectives;
        int notSet = 0;
        while (currentCost < maxPoints && notSet < 51) {
            Collections.shuffle(randomObjectives);
            String objective = randomObjectives.get(random.nextInt(randomObjectives.size()));
            int objectiveCost = jobObjectives.unsafeGet(objective);
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