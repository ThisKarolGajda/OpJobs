package me.opkarol.experience;

import me.opkarol.opc.OpAPI;

import java.io.Serializable;

public final class Experience implements Serializable {
    private int level;
    private int experience;

    public Experience(int level, int experience) {
        this.level = level;
        this.experience = experience;
    }

    public int calculateExperiencePointsForCurrentLevel() {
        // POINTS BRACKETS (required for each level):
        // 1-9Lvl: level * 11
        // 10Lvl: 100
        // 11-99Lvl: level * 12
        // 100Lvl: 1_200
        // 101-999Lvl: level * 14
        // 1000Lvl: 14_000
        // 1000+Lvl: level * 15

        if (level < 10) {
            return level * 11;
        } else if (level == 10) {
            return 100;
        } else if (level < 100) {
            return level * 12;
        } else if (level == 100) {
            return 1200;
        } else if (level < 1000) {
            return level * 14;
        } else if (level == 1000) {
            return 14000;
        } else {
            return level * 15;
        }
    }

    public int calculateRequiredPointsForNextLevel() {
        return calculateExperiencePointsForCurrentLevel() - experience;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }


    public void addExperience(int experiencePoints) {
        int current = experiencePoints;
        while (true) {
            int requiredPoints = calculateRequiredPointsForNextLevel();
            OpAPI.logInfo(current + " - " + requiredPoints + " - " + level + " - " + experience);
            if (current > requiredPoints) {
                level++;
                current = current - requiredPoints;
            } else {
                experience = current;
                break;
            }
        }
    }
}


