package me.opkarol.jobs.database;

import me.opkarol.OpJobs;
import me.opkarol.jobs.JobEvent;
import me.opkarol.jobs.JobObjective;
import me.opkarol.jobs.JobProfile;
import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.list.OpList;
import me.opkarol.opc.api.tools.HeadManager;
import me.opkarol.opc.api.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobProfilesDatabase extends Configuration {
    private final OpList<JobProfile> profiles = new OpList<>();

    public JobProfilesDatabase() {
        super(OpJobs.getInstance(), "jobs");
        setupProfiles();
    }

    public void setupProfiles() {
        useSectionKeys("", job -> {
            String displayName = getString(job + ".display");
            List<JobObjective> objectives = new ArrayList<>();
            useSectionKeys(job + ".objectives", objective -> {
                String string = getString(job + ".objectives." + objective);
                if (string.contains("|")) {
                    // 2 parameters: POINTS AND DISPLAY NAME
                    String[] strings = string.split("\\|");
                    if (strings.length != 2) {
                        return;
                    }
                    int points = StringUtil.getIntFromString(strings[0]);
                    String display = strings[1];
                    objectives.add(new JobObjective(objective, points, display));
                } else {
                    int points = StringUtil.getIntFromString(string);
                    objectives.add(new JobObjective(objective, points));
                }
            });
            // Sort based on amount of points given
            objectives.sort((o1, o2) -> o2.getPoints() - o1.getPoints());
            List<String> events = getConfig().getStringList(job + ".events");
            String eventDisplay = getString(job + ".event_display");
            JobEvent event = new JobEvent(events, eventDisplay);
            String icon = getString(job + ".head_icon");
            this.profiles.add(new JobProfile(job, displayName, objectives, event, HeadManager.getHeadFromMinecraftValueUrl(icon)));
        });
    }

    public OpList<JobProfile> getProfiles() {
        return profiles;
    }

    public Optional<JobProfile> getProfile(String job) {
        return getProfiles().stream().filter(jobProfile -> jobProfile.job().equals(job)).findAny();
    }
}
