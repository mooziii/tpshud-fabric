package me.obsilabor.tpshud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(System.getProperty("user.dir") + "/config", "tpshud_v3.json");
    private static TpsHudConfig config;

    public static void init() {
        if (!CONFIG_FILE.getParentFile().exists()) {
            CONFIG_FILE.getParentFile().mkdirs();
        }

        if (!CONFIG_FILE.exists()) {
            config = TpsHudConfig.createDefault();
            saveConfig();
        } else {
            loadConfig();
        }
    }

    public static void loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            config = GSON.fromJson(reader, TpsHudConfig.class);
            if (config == null) {
                config = TpsHudConfig.createDefault();
            }
        } catch (IOException e) {
            config = TpsHudConfig.createDefault();
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TpsHudConfig getConfig() {
        return config;
    }
}
