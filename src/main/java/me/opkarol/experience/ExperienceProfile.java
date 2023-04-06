package me.opkarol.experience;

import me.opkarol.OpJobs;
import me.opkarol.jobs.JobProfile;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlConstructor;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlIdentification;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlTable;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlValue;
import me.opkarol.opc.api.database.mysql.reflection.types.MySqlObjectValues;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

@MySqlTable(name = "opjobs")
public class ExperienceProfile implements Serializable {
    @MySqlValue(value = MySqlObjectValues.IDENTIFICATION_OBJECT, parameter = 2)
    private int id;
    @MySqlValue(value = {MySqlObjectValues.UUID_OBJECT, MySqlObjectValues.COMPARABLE_OBJECT}, parameter = 0)
    private final UUID uuid;
    @MySqlValue(parameter = 1)
    private String formattedExperience;

    private final OpMap<String, Experience> EXPERIENCE_MAP = new OpMap<>();

    @MySqlConstructor
    public ExperienceProfile(UUID uuid, String formattedExperience, Integer id) {
        this.uuid = uuid;
        this.formattedExperience = formattedExperience;
        this.id = id;
        saveFormattedStringIntoMap();
    }

    public ExperienceProfile(UUID uuid, String formattedExperience) {
        this.uuid = uuid;
        this.formattedExperience = formattedExperience;
        this.id = -1;
        saveFormattedStringIntoMap();
    }

    public void loadFormattedStringFromMap() {
        formattedExperience = specialStringValueOfMap(EXPERIENCE_MAP);
    }

    private @NotNull String specialStringValueOfMap(@NotNull OpMap<String, Experience> map) {
        StringBuilder builder = new StringBuilder();

        for (String job : map.keySet()) {
            Experience experience = map.unsafeGet(job);
            builder.append(job).append(":").append(experience.toString()).append(";");
        }

        return builder.toString();
    }

    public void saveFormattedStringIntoMap() {
        getJobsWithExperience().getMap().forEach(EXPERIENCE_MAP::set);
    }

    public OpMap<String, Experience> getJobsWithExperience() {
        OpMap<String, Experience> tempMap = new OpMap<>();
        if (formattedExperience == null || formattedExperience.isBlank()) {
            for (JobProfile profile : OpJobs.getInstance().getJobProfilesDatabase().getProfiles()) {
                tempMap.set(profile.job(), ExperienceDatabase.DEFAULT_EXPERIENCE);
            }
            return tempMap;
        }

        for (String jobString : formattedExperience.split(";")) {
            String[] strings = jobString.split(":");
            if (strings.length == 0) {
                throw new RuntimeException("Couldn't receive information about " + jobString + " profile!");
            }
            String job = strings[0];
            if (strings.length == 1) {
                tempMap.set(job, ExperienceDatabase.DEFAULT_EXPERIENCE);
            }
            Experience experience = new Experience(strings[1]);
            tempMap.set(job, experience);
        }
        return tempMap;
    }

    public UUID getUuid() {
        return uuid;
    }

    @MySqlIdentification
    public void setId(Integer id) {
        this.id = id;
    }

    public Experience getExperience(String job) {
        return EXPERIENCE_MAP.getOrDefault(job, ExperienceDatabase.DEFAULT_EXPERIENCE);
    }

    public void setExperience(String job, Experience experience) {
        EXPERIENCE_MAP.set(job, experience);
        loadFormattedStringFromMap();
    }
}

