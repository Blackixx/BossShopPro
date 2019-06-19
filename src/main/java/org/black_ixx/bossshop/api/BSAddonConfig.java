package org.black_ixx.bossshop.api;

import com.google.common.io.Files;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BSAddonConfig implements BSAddonStorage {

    boolean isSaving = false;
    private Plugin plugin;
    private File file;
    private YamlConfiguration config;

    public BSAddonConfig(Plugin plugin, String file_name) {
        this.plugin = plugin;
        file = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/addons/" + plugin.getName() + "/" + file_name + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public BSAddonConfig(Plugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Save the addon config
     * @return saved or not
     */
    public boolean save() {
        if (isSaving)
            return false;

        isSaving = true;

        try {
            config.save(file);
        } catch (IOException e1) {
            plugin.getLogger().warning("File I/O Exception on saving " + file.getName());
            e1.printStackTrace();
            return false;
        }

        isSaving = false;
        return true;
    }

    /**
     * Save the addon config async
     */
    public void saveAsync() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {

            @Override
            public void run() {
                save();
            }

        });
    }

    /**
     * Reload the addon config
     * @return reloaded or not
     */
    public boolean reload() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get the addon config
     * @return addon config
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get the config file
     * @return file
     */
    public File getFile() {
        return file;
    }


    /**
     * Set a path for the config
     * @param path path
     * @param value value
     */
    @Override
    public void set(String path, Object value) {
        config.set(path, value);
    }

    /**
     * Get a string from the config
     * @param path path
     * @param def default
     * @return string
     */
    @Override
    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    /**
     * Get an int from the config
     * @param path path
     * @param def default
     * @return int
     */
    @Override
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    /**
     * Get a double from the config
     * @param path path
     * @param def default
     * @return double
     */
    @Override
    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    /**
     * Get a boolean from the config
     * @param path path
     * @param def default
     * @return boolean
     */
    @Override
    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    /**
     * Get a string list from the config
     * @param path path
     * @return string list
     */
    @Override
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    /**
     * Add a default path to the config
     * @param path path
     * @param value value
     */
    public void addDefault(String path, Object value) {
        if (!config.contains(path)) {
            config.set(path, value);
        }
    }

    /**
     * Check if a config contains a path
     * @param key path
     * @return contains or not
     */
    @Override
    public boolean containsPath(String key) {
        return config.contains(key);
    }

    /**
     * List all keys in a section of a config
     * @param section the section to check
     * @param deep subkeys or not
     * @return list of keys
     */
    @Override
    public Set<String> listKeys(String section, boolean deep) {
        ConfigurationSection s = config;
        if (section != null) {
            s = s.getConfigurationSection(section);
        }
        if (s != null) {
            return s.getKeys(deep);
        }
        return null;
    }

    /**
     * Delete all parts of a section
     * @param section section to delete
     */
    @Override
    public void deleteAll(String section) {
        ConfigurationSection s = config;
        if (section != null) {
            s = s.getConfigurationSection(section);
        }
        if (s != null) {
            for (String key : s.getKeys(true)) {
                s.set(key, null);
            }
            config.set(s.getCurrentPath(), null);
        }
    }

    /**
     * Copy a file to a new file
     * @param source the source
     * @return new file
     */
    @Override
    public boolean pasteContentFrom(BSAddonStorage source) {
        if (source instanceof BSAddonConfig) {
            BSAddonConfig c = (BSAddonConfig) source;
            try {
                source.save();
                Files.copy(c.getFile(), file);
                reload();
            } catch (IOException e) {
                ClassManager.manager.getBugFinder().warn("Unable to copy storage file from '" + c.getFile().getPath() + "' to '" + file.getPath() + "'.");
                return false;
            }
            return true;
        }

        //New storage types might be added in future
        return false;
    }

}
