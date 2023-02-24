package me.opkarol.jobs.database;

import me.opkarol.OpJobs;
import me.opkarol.jobs.JobTask;
import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.map.OpMap;

public class ObjectivesDatabase extends Configuration {
    private final OpMap<JobTask, OpMap<String, Integer>> objectives = new OpMap<>();
    private static ObjectivesDatabase INSTANCE;

    {
        INSTANCE = this;
    }

    public ObjectivesDatabase() {
        super(OpJobs.getInstance(), "jobs");
        setupJobs();
    }

    public static ObjectivesDatabase getInstance() {
        return INSTANCE;
    }

    public void setupJobs() {
        useSectionKeys("", string -> {
            OpMap<String, Integer> tempMap = new OpMap<>();
            useSectionKeys(string + ".objectives", objective -> {
                tempMap.set(objective, getInt(string + ".objectives." + objective));
            });
            this.objectives.set(JobTask.valueOf(string), tempMap);
        });
    }

    public OpMap<JobTask, OpMap<String, Integer>> getObjectives() {
        return objectives;
    }
}
