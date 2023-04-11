package me.opkarol.experience;

import me.opkarol.opc.api.utils.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
        // 1-9Lvl: level * 21
        // 11-99Lvl: level * 22
        // 101-499Lvl: level * 23
        // 500-999Lvl: level * 24
        // 1000+Lvl: level * 25

        if (level < 10) {
            return level * 21;
        }   else if (level < 100) {
            return level * 22;
        } else if (level < 500) {
            return level * 23;
        } else if (level < 1000) {
            return level * 24;
        } else {
            return level * 25;
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
            int requiredPoints = calculateExperiencePointsForCurrentLevel();
            if (current >= requiredPoints) {
                level++;
                current = current - requiredPoints;
            } else {
                experience = current;
                break;
            }
        }
    }

    public @NotNull String getPercentage() {
        double number = experience * 100d / calculateExperiencePointsForCurrentLevel();
        number = Math.round(number * 100);
        return number/100 + "%";
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        //[level:]10,[experience:]10
        return level + "," + experience;
    }

    public Experience(@NotNull String fromString) {
        String[] split = fromString.split(",");
        level = StringUtil.getIntFromString(split[0]);
        experience = StringUtil.getIntFromString(split[1]);
    }
}


