package me.opkarol.experience;

import me.opkarol.OpJobs;
import me.opkarol.jobs.JobProfile;
import me.opkarol.jobs.database.JobProfilesDatabase;
import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class ExperienceDatabase {
    public static final Experience DEFAULT_EXPERIENCE = new Experience(1, 0);
    private final IDefaultDatabase<ExperienceProfile, UUID> database;

    public ExperienceDatabase(@NotNull OpJobs opJobs) {
        // Load database object
        database = opJobs.getLocalDatabase();
    }

    public ExperienceProfile getProfile(UUID uuid) {
        return database.get(uuid, uuid)
                .orElse(new ExperienceProfile(uuid, getDefaultFormattedExperience()));
    }

    public Experience getExperience(UUID uuid, @NotNull String jobTask) {
        ExperienceProfile profile = getProfile(uuid);
        return profile
                .getExperience(jobTask.toUpperCase());
    }

    public void setExperience(UUID uuid, String job, Experience experience) {
        ExperienceProfile experienceProfile = getProfile(uuid);
        experienceProfile.setExperience(job, experience);
        database.add(uuid, experienceProfile);
    }

    private @NotNull String getDefaultFormattedExperience() {
        StringBuilder builder = new StringBuilder();
        JobProfilesDatabase profilesDatabase = OpJobs.getInstance().getJobProfilesDatabase();
        for (JobProfile profile : profilesDatabase.getProfiles()) {
            builder.append(profile.job()).append(":").append(DEFAULT_EXPERIENCE).append(";");
        }
        return builder.toString();
    }

    public Optional<Experience> getHighestLevelExperience(UUID uuid) {
        return getProfile(uuid).getJobsWithExperience().getValuesStream().max(Comparator.comparing(Experience::getLevel));
    }

    public int getSumLevelExperience(UUID uuid) {
        final int[] sum = {0};
        getProfile(uuid).getJobsWithExperience().getMap()
                .forEach((s, experience) -> sum[0] += experience.getLevel());
        return sum[0];
    }
}