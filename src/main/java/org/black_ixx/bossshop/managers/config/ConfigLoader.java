package org.black_ixx.bossshop.managers.config;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigLoader {

    public static YamlConfiguration loadConfiguration(File file, boolean debug) throws InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            if (debug)
                Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (IOException ex) {
            if (debug)
                Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }

        return config;
    }

}
