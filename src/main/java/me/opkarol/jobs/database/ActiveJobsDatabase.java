package me.opkarol.jobs.database;

import me.opkarol.jobs.Job;
import me.opkarol.opc.api.database.flat.FlatDatabase;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.plugins.OpPlugin;
import me.opkarol.opc.api.tools.autostart.IDisable;
import me.opkarol.opc.api.tools.autostart.OpAutoDisable;

import java.util.UUID;

public class ActiveJobsDatabase extends FlatDatabase<OpMap<UUID, Job>> implements IDisable {
    private static ActiveJobsDatabase INSTANCE;
    private OpMap<UUID, Job> map;

    {
        INSTANCE = this;
    }

    public ActiveJobsDatabase(OpPlugin plugin) {
        super(plugin, "active-database.db");
        map = loadObject();
        if (map == null) {
            this.map = new OpMap<>();
        }
        OpAutoDisable.add(this);
    }

    public static ActiveJobsDatabase getInstance() {
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        saveObject(map);
    }

    public OpMap<UUID, Job> getMap() {
        return map;
    }
}
