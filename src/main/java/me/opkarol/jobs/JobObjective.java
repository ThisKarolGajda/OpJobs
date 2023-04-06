package me.opkarol.jobs;

import java.io.Serializable;

public final class JobObjective implements Serializable {
    private final String name;
    private final Integer points;
    private String display;

    public JobObjective(String name, Integer points, String display) {
        this.name = name;
        this.points = points;
        this.display = display;
    }

    public JobObjective(String name, Integer points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public Integer getPoints() {
        return points;
    }

    public String getDisplayName() {
        return display;
    }

    public void setDisplayName(String display) {
        this.display = display;
    }

    public String getFixedName() {
        return getDisplayName() == null ? getName() : getDisplayName();
    }
}