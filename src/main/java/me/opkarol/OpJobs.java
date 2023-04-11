package me.opkarol;


import me.opkarol.commands.JobCommand;
import me.opkarol.experience.ExperienceDatabase;
import me.opkarol.experience.ExperienceProfile;
import me.opkarol.jobs.JobListener;
import me.opkarol.jobs.database.ActiveJobsDatabase;
import me.opkarol.jobs.database.JobProfilesDatabase;
import me.opkarol.jobs.inventories.JobChooseInventory;
import me.opkarol.jobs.inventories.JobExperienceInventory;
import me.opkarol.opc.api.extensions.PlaceholderAPI;
import me.opkarol.opc.api.gui.holder.InventoriesHolder;
import me.opkarol.opc.api.plugins.OpDatabaseMessagesPlugin;

import java.util.UUID;
import java.util.function.Function;

import static me.opkarol.experience.ExperienceDatabase.DEFAULT_EXPERIENCE;

public class OpJobs extends OpDatabaseMessagesPlugin<ExperienceProfile, UUID> {

    private static OpJobs instance;
    private final ActiveJobsDatabase activeJobsDatabase = new ActiveJobsDatabase(this);
    private ExperienceDatabase experienceDatabase;
    private JobProfilesDatabase jobProfilesDatabase;
    private InventoriesHolder inventoriesHolder;
    private TokenManagerExtension tokenManagerExtension;

    {
        instance = this;
    }

    public static OpJobs getInstance() {
        return instance;
    }

    @Override
    public Object[] registerCommands() {
        return new Object[]{
                new JobCommand(inventoriesHolder),
        };
    }

    @Override
    public void enable() {
        new JobListener(activeJobsDatabase);
        jobProfilesDatabase = new JobProfilesDatabase();
        experienceDatabase = new ExperienceDatabase(this);
        inventoriesHolder = new InventoriesHolder();
        tokenManagerExtension = new TokenManagerExtension();

        //Inventories
        new JobChooseInventory();
        new JobExperienceInventory();

        enablePAPI();
    }

    public void enablePAPI() {
        PlaceholderAPI.registerExtension("opjobs", "ThisOpKarol", "1.0.0", (offlinePlayer, identifier) -> {
            if (offlinePlayer == null) {
                return "";
            }
            UUID uuid = offlinePlayer.getUniqueId();

            if (identifier.equals("highest_level")) {
                return String.valueOf(experienceDatabase.getHighestLevelExperience(uuid).orElse(DEFAULT_EXPERIENCE).getLevel());
            }

            if (identifier.equals("sum_level")) {
                return String.valueOf(experienceDatabase.getSumLevelExperience(uuid));
            }

            return String.valueOf(experienceDatabase.getExperience(uuid, identifier).getLevel());
        }).isEnabled();
    }

    public ActiveJobsDatabase getActiveJobsDatabase() {
        return activeJobsDatabase;
    }

    public ExperienceDatabase getExperienceDatabase() {
        return experienceDatabase;
    }

    public JobProfilesDatabase getJobProfilesDatabase() {
        return jobProfilesDatabase;
    }

    public InventoriesHolder getInventoriesHolder() {
        return inventoriesHolder;
    }

    public TokenManagerExtension getTokenManagerExtension() {
        return tokenManagerExtension;
    }

    @Override
    public String getFlatFileName() {
        return "database.db";
    }

    @Override
    public Function<ExperienceProfile, UUID> getBaseFunction() {
        return ExperienceProfile::getUuid;
    }

    @Override
    public Class<? extends ExperienceProfile> getClassInstance() {
        return ExperienceProfile.class;
    }
}




