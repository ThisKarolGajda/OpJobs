package me.opkarol;


import me.opkarol.commands.JobCommand;
import me.opkarol.experience.ExperienceDatabase;
import me.opkarol.experience.ExperienceProfile;
import me.opkarol.jobs.JobListener;
import me.opkarol.jobs.database.ActiveJobsDatabase;
import me.opkarol.jobs.database.JobProfilesDatabase;
import me.opkarol.jobs.inventories.JobChooseInventory;
import me.opkarol.jobs.inventories.JobExperienceInventory;
import me.opkarol.opc.api.gui.holder.InventoriesHolder;
import me.opkarol.opc.api.plugins.OpDatabaseMessagesPlugin;

import java.util.UUID;
import java.util.function.Function;

public class OpJobs extends OpDatabaseMessagesPlugin<ExperienceProfile, UUID> {

    private static OpJobs instance;
    private final ActiveJobsDatabase activeJobsDatabase = new ActiveJobsDatabase(this);
    private ExperienceDatabase experienceDatabase;
    private JobProfilesDatabase jobProfilesDatabase;
    private InventoriesHolder inventoriesHolder;

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

        //Inventories
        new JobChooseInventory();
        new JobExperienceInventory();
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




