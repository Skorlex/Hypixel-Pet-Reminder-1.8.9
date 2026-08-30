package com.github.skorlex.hypixelpetreminder;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class PetConfig {
    private static Configuration config;
    public static long targetTime = 0L;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        loadConfig();
    }

    private static void loadConfig() {
        config.load();

        // Forge 1.8.9 doesn't have getLong(), so we load the timestamp as a String
        String timeStr = config.get("General", "targetTime", "0").getString();
        try {
            targetTime = Long.parseLong(timeStr);
        } catch (NumberFormatException e) {
            targetTime = 0L;
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void saveTargetTime(long newTime) {
        targetTime = newTime;
        // Save the massive timestamp number as a String to prevent data loss
        config.get("General", "targetTime", "0").set(String.valueOf(newTime));
        config.save();
    }
}