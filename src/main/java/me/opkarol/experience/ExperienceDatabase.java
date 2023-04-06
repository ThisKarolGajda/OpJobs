package me.opkarol.experience;

import me.opkarol.OpJobs;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;

import java.util.UUID;

public class ExperienceDatabase {
    public static final Experience DEFAULT_EXPERIENCE = new Experience(1, 0);
    private final IDefaultDatabase<ExperienceProfile, UUID> database;

    public ExperienceDatabase(OpJobs opJobs) {
        // Load
        database = opJobs.getLocalDatabase();
    }

    public ExperienceProfile getProfile(UUID uuid) {
        return database.get(uuid, uuid)
                .orElse(new ExperienceProfile(uuid, ""));
    }

    public Experience getExperience(UUID uuid, String jobTask) {
        ExperienceProfile profile = getProfile(uuid);
        return profile
                .getExperience(jobTask);
    }

    public void setExperience(UUID uuid, String job, Experience experience) {
        ExperienceProfile experienceProfile = getProfile(uuid);
        experienceProfile.setExperience(job, experience);
        database.add(uuid, experienceProfile);
    }
}
