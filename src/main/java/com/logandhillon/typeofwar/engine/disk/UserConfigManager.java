package com.logandhillon.typeofwar.engine.disk;

import com.logandhillon.typeofwar.networking.proto.ConfigProto.UserConfig;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Handles (de)serialization of save game data and (un)loading said data to the disk.
 *
 * @author Logan Dhillon
 */
public class UserConfigManager {
    private static final Logger     LOG            = LoggerContext.getContext().getLogger(UserConfigManager.class);
    private static final File       FILE           = new File("typeofwar.dat");
    private static final UserConfig DEFAULT_CONFIG = UserConfig.newBuilder()
                                                               .setName(System.getProperty("user.name"))
                                                               .setColorIdx(0)
                                                               .build();

    /**
     * Saves the provided {@link UserConfig} to the disk.
     */
    public static void save(UserConfig config) {
        try (FileOutputStream file = new FileOutputStream(FILE)) {
            if (FILE.getParent() != null) {
                LOG.warn("Parent directory for user config file doesn't exist, creating folder(s).");
                new File(FILE.getParent()).mkdirs();
            }

            LOG.info("Writing user configuration to {}", FILE.getAbsolutePath());
            config.writeTo(file);
        } catch (IOException e) {
            LOG.error("Failed to load user configuration from disk", e);
        }
    }

    /**
     * Tries to load the {@link UserConfig} from disk, returning the {@link UserConfigManager#DEFAULT_CONFIG} if it (a)
     * fails or (b) doesn't exist.
     *
     * @return user config from disk
     */
    public static UserConfig load() {
        // if there is no save file, save the default and return
        if (!FILE.exists()) {
            LOG.warn("Saved user config doesn't exist, saving default config to disk");
            save(DEFAULT_CONFIG);
            return DEFAULT_CONFIG;
        }

        try (FileInputStream file = new FileInputStream(FILE)) {
            var c = UserConfig.parseFrom(file);
            LOG.info("Successfully loaded user config for '{}' from disk", c.getName());
            return c;
        } catch (IOException e) {
            LOG.error("Failed to load user configuration from disk", e);
            return DEFAULT_CONFIG;
        }
    }
}
