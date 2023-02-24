package me.opkarol;


import me.opkarol.commands.JobCommand;
import me.opkarol.experience.ExperienceDatabase;
import me.opkarol.jobs.JobListener;
import me.opkarol.jobs.database.ActiveJobsDatabase;
import me.opkarol.jobs.database.ObjectivesDatabase;
import me.opkarol.opc.api.plugins.OpMessagesPlugin;

public class OpJobs extends OpMessagesPlugin {

    private static OpJobs instance;
    private final ActiveJobsDatabase activeJobsDatabase = new ActiveJobsDatabase(this);

    {
        instance = this;
    }

    public static OpJobs getInstance() {
        return instance;
    }

    @Override
    public boolean registerCommandsWithBrigadier() {
        getCommandHandler().register(new JobCommand());
        return true;
    }

    @Override
    public void enable() {
        new JobListener(activeJobsDatabase);
        ExperienceDatabase experienceDatabase = new ExperienceDatabase();
        ObjectivesDatabase objectivesDatabase = new ObjectivesDatabase();
    }

    @Override
    public void disable() {

    }

}
