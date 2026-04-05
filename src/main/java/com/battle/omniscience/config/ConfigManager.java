package com.battle.omniscience.config;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.battle.omniscience.Omniscience;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigManager {
    /** Indicates whether the configuration subsystem has been initialized. */
    private static boolean initialized = false;

    private static Config config = null;
    private static final Config defaults = new Config();

    private static Gson gson;
    private static File configFile;

    private static final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Initializes the configuration manager and loads config from disk.
     */
    public static void init() {
        if (initialized) {
            return;
        }

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), Omniscience.CONFIG_FILE_NAME);
        readConfig(false);

        initialized = true;
    }

    /**
     * Reads the config from disk, either synchronously or asynchronously.
     */
    public static void readConfig(boolean async) {
        Runnable task = () -> {
            try {
                if (configFile.exists()) {
                    String fileContents = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
                    config = gson.fromJson(fileContents, Config.class);

                    if (config.validate()) {
                        writeConfig(true);
                    }
                } else {
                    writeNewConfig();
                }
            } catch (Exception e) {
                e.printStackTrace();
                writeNewConfig();
            }
        };

        if (async) {
            executor.execute(task);
        } else {
            task.run();
        }
    }

    /**
     * Creates a new default configuration and writes it immediately.
     */
    public static void writeNewConfig() {
        config = new Config();
        writeConfig(false);
    }

    /**
     * Writes the current configuration to disk.
     */
    public static void writeConfig(boolean async) {
        Runnable task = () -> {
            try {
                if (config != null) {
                    String serialized = gson.toJson(config);
                    FileUtils.writeStringToFile(configFile, serialized, StandardCharsets.UTF_8);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (async) {
            executor.execute(task);
        } else {
            task.run();
        }
    }

    public static Config getConfig() {
        return config;
    }

    public static Config getDefaults() {
        return defaults;
    }
}
