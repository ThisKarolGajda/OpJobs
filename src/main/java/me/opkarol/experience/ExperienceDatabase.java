package me.opkarol.experience;

import me.opkarol.OpJobs;
import me.opkarol.opc.api.database.flat.FlatDatabase;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.tools.autostart.IDisable;
import me.opkarol.opc.api.tools.autostart.OpAutoDisable;

import java.util.UUID;

public class ExperienceDatabase extends FlatDatabase<OpMap<UUID, Experience>> implements IDisable {
    private OpMap<UUID, Experience> map;
    private static ExperienceDatabase INSTANCE;

    {
        INSTANCE = this;
    }

    public ExperienceDatabase() {
        super(OpJobs.getInstance(), "experience.db");
        this.map = loadObject();
        if (map == null) {
            this.map = new OpMap<>();
        }
        OpAutoDisable.add(this);
    }

    public static ExperienceDatabase getInstance() {
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        saveObject(this.map);
    }

    public OpMap<UUID, Experience> getMap() {
        return map;
    }

    public Experience getExperience(UUID uuid) {
        if (getMap().containsKey(uuid)) {
            return getMap().unsafeGet(uuid);
        }
        return new Experience(1, 0);
    }
}
